/**
 * ECSE 489 - Telecommunication Networks Lab
 * Winter 2015
 * Professor Coates
 * 
 * @author Singzon, Ryan			260397455
 * @author Muralidharan, Keertana	260409960
 */

package telecom_lab3;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Client { 
	
	static int port = 5000;
	static boolean loggedIn = false;
	
	static String ip = null;
	static Socket socket = null;
	static User user = null;
	static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	static DataInputStream input;
	
	public static void main(String args[]){		

		try{
			InetAddress address = InetAddress.getByName("ecse-489.ece.mcgill.ca");
			ip = address.getHostAddress();
			
			// Main loop of program, accepts and parses commands
			while(true){
				String option = null;
				String username = null;
				String password = null;
				
				// If logged out, prompt the user for username and password
				if(!loggedIn){
					try{
						
						// Give user the option of logging in or creating a new user
						System.out.println("Type \"login\" to log in, or \"create\" to create a new user");
						option = reader.readLine();
						
						
						// No valid option selected, prompt user for selection again
						if( !option.toLowerCase().equals("l") 		&&
							!option.toLowerCase().equals("login") 	&&
							!option.toLowerCase().equals("c") 		&& 
							!option.toLowerCase().equals("create") 	){
								
							System.out.println("Invalid selection '"+ option +"'");
							continue;
						}
						
						else {
							// Obtain username and password from user
							System.out.println("Username");
							username = reader.readLine();
							System.out.println("Password");
							password = reader.readLine();
							
							// Connect to the server
							socket = new Socket(ip, port);

							// Login 
							if(option.toLowerCase().equals("l") || option.toLowerCase().equals("login")){
								
								user = new User(username, password, socket);
								user.login(username, password);
							}
							
							// Create user
							else{
								user = new User(username, password, socket);
								user.createUser(username, password);
							}
							
							int submessageType = parseResponse();
														
							switch(submessageType){
							case 0:
								System.out.println("Logging in..");
								loggedIn = true;
								break;
							case 1:
								System.out.println("This user is already logged in");
								break;
							case 2:
								System.out.println("Invalid username or password");
								break;
							case 3:
								System.out.println("Missing username or password");
								break;
							default:
								System.out.println("An unknown error occurred");
							}
							
							
							// TODO: Start a thread to keep track of time and every 1 second, call the user.queryMessages() function
						}
					} 
					
					// Handle exceptions
					catch (UnknownHostException e) {
						System.out.println("Sock:" + e.getMessage());
					} catch (EOFException e) {
						System.out.println("EOF:" + e.getMessage());
					} catch (IOException e) {
						System.out.println("IO:" + e.getMessage());
					} finally {
						if (socket != null) {
							try {
								socket.close();
							} catch (IOException e) {
								// Socket close failed
							}
						}
					}
				}
				
				// User is logged in, wait for them to enter a command
				else{
					System.out.println("Enter a command");
					String command = null;
					
					try{
						command = reader.readLine();
						parseCommand(command);
						System.out.println("You entered: "+command);
					} 
					
					catch (IOException e){
						e.printStackTrace();
					}
				}
			}
			
		} catch(UnknownHostException e){
			System.out.println("Unknown host: " + e.getStackTrace());
		}
	}
	
	/**
	 * Parses the user input and calls the functions to perform the command
	 * @param command
	 * @return
	 */
	private static void parseCommand(String command){
		
		String commandLowercase = command.toLowerCase();
		
		switch(commandLowercase){
	
		case "echo":
			System.out.println("Enter the text you want to echo:");
			
			try{
				String echoText = reader.readLine();
				user.echo(echoText);
				
			} catch(IOException e){
				e.printStackTrace();
			}
			
			break;
		
		case "send":
			
			try{
				System.out.println("Destination username");
				String destinationUser = reader.readLine();
				System.out.println("Message");
				String message = reader.readLine();

				user.sendMessageToUser(destinationUser, message);
				
			} catch(IOException e){
				e.printStackTrace();
			}
			
			break;
			
		case "query":
			user.queryMessages();
			break;
			
		case "exit":
			user.exit();
			loggedIn = false;
			break;
			
		default:
			System.out.println("Unrecognized command: "+command);
			System.out.println(
				"Usage: \n"+
				"echo : Server returns the same text sent to it\n"+
				"send : Send a message to an existing user\n"+
				"query: Retrieve messages that have been sent to the user currently logged in\n"+
				"exit : Logs off the current user\n");
			break;
		}
	}
	
	/**
	 * Parses the response from the server and informs user of status
	 * @return submessage type
	 */
	private static int parseResponse(){

		byte[] readBuf = new byte[1000];
		int bytesReceived = 0;
		int bytes = 0;

		
		try{
			// Wait for login success message from server
			input = new DataInputStream(socket.getInputStream());

			// Copy bytes into buffer
			bytes = input.read(readBuf);
			
			// Extract the response from the byte array
			// Extract the message from the byte array
			byte[] typeBytes = Arrays.copyOfRange(readBuf, 0, 4);
			byte[] submessageTypeBytes = Arrays.copyOfRange(readBuf, 4,	8);
			byte[] sizeBytes = Arrays.copyOfRange(readBuf, 8, 12);
			
			int messageType = ByteBuffer.wrap(typeBytes).getInt();
			int submessageType = ByteBuffer.wrap(submessageTypeBytes).getInt();
			int dataSize = ByteBuffer.wrap(sizeBytes).getInt();
			
			byte[] dataBytes = Arrays.copyOfRange(readBuf, 12, 12 + dataSize);
			String data = new String(dataBytes);
			
			System.out.println("Response: ");
			System.out.println("Type: "+ messageType);
			System.out.println("Submessage: "+submessageType);
			System.out.println("Size: "+dataSize);
			System.out.println("Data: "+data);
			return submessageType;
			
		} catch (IOException e){
			System.out.println("IO Exception on parsing server response. " + e.getStackTrace());
		}

		return -1;
	}
}
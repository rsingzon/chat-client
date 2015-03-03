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

public class Client { 
	
	static int port = 5000;
	static boolean loggedIn = false;
	
	static String ip = null;
	static Socket socket = null;
	static User user = null;
	static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

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
						if( !option.toLowerCase().equals("l") 		||
							!option.toLowerCase().equals("login") 	||
							!option.toLowerCase().equals("c") 		|| 
							!option.toLowerCase().equals("create") 	){
								
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
							
							// Wait for login success message from server
							// TODO Retrieve the response message
							
							loggedIn = true;
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
				"Usage: "+
				"echo : Server returns the same text sent to it"+
				"send : Send a message to an existing user"+
				"query: Retrieve messages that have been sent to the user currently logged in"+
				"exit : Logs off the current user");
			break;
		}
	}
}
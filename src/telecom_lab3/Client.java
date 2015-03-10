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
	
	static int port = 5004;
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
						System.out.println("\n------------------------------------------------------------");
						System.out.println("Type \"login\" to log in, or \"create\" to create a new user");
						option = reader.readLine();
						
						if(option.toLowerCase().equals("exit")  || option.toLowerCase().equals("quit")){
							System.exit(0);
						}
						
						// No valid option selected, prompt user for selection again
						else if( 
							!option.toLowerCase().equals("l") 		&&
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

								// Check if the username is properly formatted and log user in
								if(user.login()){
									Message response = user.parseResponse();
									int submessageType = response.getSubmessageType();
									String data = response.getDataString();
									System.out.println(data);
									
									// Successful login
									if(submessageType == 0){
										loggedIn = true;
										user.isLoggedIn = true;
									} 
									
									// User is already logged in
									// Invalid username or password
									// Missing username or password
									else if(submessageType == 1 ||
											submessageType == 2 || 
											submessageType == 3 ){
										continue;
									} 
								}
								
								// Username is badly formatted
								else{
									continue;
								}
							}
							
							// Create user
							else{
								user = new User(username, password, socket);
								
								// Check if username is properly formatted and create user
								if(user.createUser()){
							
									Message response = user.parseResponse();
									int submessageType = response.getSubmessageType();
									String data = response.getDataString();
									System.out.println("\n"+data );
									
									// Successfully created user
									// User already exists
									if(submessageType == 0 || submessageType == 1){
									} 
									
									// User is already logged in
									// Badly formatted request
									else if(submessageType == 2 || submessageType == 3){
										continue;
									} 
									
									// Log user in
									user.login();
									
									response = user.parseResponse();
									submessageType = response.getSubmessageType();
									data = response.getDataString();
									System.out.println("\n"+data);
									
									// Successfully logged in
									if(submessageType == 0){
										loggedIn = true;
									} 
									
									// User already logged in
									// Bad credentials
									// Badly formatted request
									else if(submessageType == 1 ||
											submessageType == 2 || 
											submessageType == 3 ){
										
										continue;
									} 
									
									user.createStore();
									
									response = user.parseResponse();
									submessageType = response.getSubmessageType();
									data = response.getDataString();
									System.out.println("\n"+data);
									
									
									// Successfully created store
									// Store already exists
									if(submessageType == 0 || submessageType == 1){
									} 
									
									// User not logged in
									else if(submessageType == 2){
										continue;
									}
								}
								
								// Username is badly formatted
								else{
									continue;
								}
							}
							QueryThread query = new QueryThread(user);
							query.start();
						}
					} 
					
					// Handle exceptions
					catch (UnknownHostException e) {
						System.out.println("Sock:" + e.getMessage());
					} catch (EOFException e) {
						System.out.println("EOF:" + e.getMessage());
					} catch (IOException e) {
						System.out.println("IO:" + e.getMessage());
					} 
				}
				
				// User is logged in, wait for them to enter a command
				else{
					System.out.println("\nEnter a command or press enter to list available commands.");
					String command = null;
					
					try{
						command = reader.readLine();
						parseCommand(command);
					} 
					
					catch (IOException e){
						e.printStackTrace();
					}
				}
			}
		} catch(UnknownHostException e){
			System.out.println("Unknown host: " + e.getMessage());
		}
	}
	
	/**
	 * Parses the user input and calls the functions to perform the command
	 * @param command
	 * @return
	 */
	private static void parseCommand(String command){
		
		String commandLowercase = command.toLowerCase();
		Message response;
		int submessageType;
		String data;
		
		switch(commandLowercase){
	
		// Sends a string to the server and the server responds with the same string
		case "echo":
			System.out.println("Enter the text you want to echo:");
			
			try{
				// Accept input from the user
				String echoText = reader.readLine();
				user.echo(echoText);
				
				// Wait for the response from the server
				response = user.parseResponse();
				submessageType = response.getSubmessageType();

				System.out.println(response.getDataString());
				
			} catch(IOException e){
				e.printStackTrace();
			}
			
			break;
		
		case "send":
			
			try{
				// Read the destination username and message from the user
				System.out.println("Destination username:");
				String destinationUser = reader.readLine();
				System.out.println("Message:");
				String message = reader.readLine();

				// Send the message to the server
				user.sendMessageToUser(destinationUser, message);
				
				// Wait for a response from the server
				response = user.parseResponse();
				submessageType = response.getSubmessageType();

				System.out.println(response.getDataString());
				
			} catch(IOException e){
				e.printStackTrace();
			}
			
			break;
			
		case "query":

			// Send query command
			user.queryMessages();
			
			// Get server response
			response = user.parseResponse();
			submessageType = response.getSubmessageType();
			System.out.println(user.formatMessage(response.getDataString()));
			
			break;
			
		case "delete":
			
			// Send delete command
			user.deleteUser();
			
			// Get server response
			response = user.parseResponse();
			submessageType = response.getSubmessageType();
			System.out.println(response.getDataString());
			
			// Send the logoff command and log the user out on the client side
			user.logoff();
			loggedIn = false;
			break;
			
		case "logoff":
			
			// Send logoff command
			user.logoff();
			
			// Get the response from the server
			response = user.parseResponse();
			submessageType = response.getSubmessageType();
			System.out.println(response.getDataString());

			// Log the user out on the client side
			user.isLoggedIn = false;
			loggedIn = false;
			break;
			
		case "exit":
			user.exit();
			try{
				socket.close();
			} catch(IOException e){
				e.printStackTrace();
			}
			System.exit(0);
			
			
			break;
			
		default:
			System.out.println("Unrecognized command: "+command);
			System.out.println(
				"Usage: \n"+
				"echo : Server returns the same text sent to it\n"+
				"send : Send a message to an existing user\n"+
				"query: Retrieve messages that have been sent to the user currently logged in\n"+
				"delete: Deletes the user that is currently logged in and logs the user out\n"+
				"logoff: Logs the current user out\n"+
				"exit : Exits the program\n");
			break;
		}
	}
}
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
								
								Message response = user.parseResponse();
								int submessageType = response.getSubmessageType();
								String data = response.getDataString();
								System.out.println(data);
																						
								if(submessageType == 0){
									System.out.println("Logging in..");
									loggedIn = true;
									user.isLoggedIn = true;
								} else if(submessageType == 1){
									System.out.println("This user is already logged in");
									continue;
								} else if(submessageType == 2){
									System.out.println("Invalid username or password");
									continue;
								} else if(submessageType == 3){
									System.out.println("Missing username or password");
									continue;
								}
							}
							
							// Create user
							else{
								user = new User(username, password, socket);
								user.createUser(username, password);
							
								Message response = user.parseResponse();
								int submessageType = response.getSubmessageType();
								String data = response.getDataString();
								System.out.println("\n"+data );
								
								if(submessageType == 0){
									System.out.println("Successfully created user");
								} else if(submessageType == 1){
									System.out.println("User already exists");
								} else if(submessageType == 2){
									System.out.println("User already logged in");
									continue;
								} else if(submessageType == 3){
									System.out.println("Badly formatted request");
									continue;
								}
								
								user.login(username, password);
								
								response = user.parseResponse();
								submessageType = response.getSubmessageType();
								data = response.getDataString();
								System.out.println("\n"+data);
								
								if(submessageType == 0){
									System.out.println("Successfully logged in");
									loggedIn = true;
								} else if(submessageType == 1){
									System.out.println("User already logged in");
									continue;
								} else if(submessageType == 2){
									System.out.println("Bad credentials");
									continue;
								} else if(submessageType == 3){
									System.out.println("Badly formatted request");
									continue;
								}
								
								user.createStore();
								
								response = user.parseResponse();
								submessageType = response.getSubmessageType();
								data = response.getDataString();
								System.out.println("\n"+data);
								
								if(submessageType == 0){
									System.out.println("Successfully created store");
								} else if(submessageType == 1){
									System.out.println("Store already exists");
								} else if(submessageType == 2){
									System.out.println("User not logged in");
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
					System.out.println("Enter a command");
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

				if(submessageType == 0){
					data = response.getDataString();
					System.out.println("Response from server: \n" + data);
				}
			} catch(IOException e){
				e.printStackTrace();
			}
			
			break;
		
		case "send":
			
			try{
				// Read the destination username and message from the user
				System.out.println("Destination username");
				String destinationUser = reader.readLine();
				System.out.println("Message");
				String message = reader.readLine();

				// Send the message to the server
				user.sendMessageToUser(destinationUser, message);
				
				// Wait for a response from the server
				response = user.parseResponse();
				submessageType = response.getSubmessageType();
				
				if(submessageType == 0){
					System.out.println("Message sent successfully!");
				} else if(submessageType == 1){
					System.out.println("Destination user does not have a data store created");
				} else if(submessageType == 2){
					System.out.println("Destination user does not exist");
				} else if(submessageType == 3){
					System.out.println("No users are currently logged in");					
				} else if(submessageType == 4){
					System.out.println("Badly formatted message, there are missing fields in the data field");
				}
				
			} catch(IOException e){
				e.printStackTrace();
			}
			
			break;
			
		case "query":
			user.queryMessages();
			
			response = user.parseResponse();
			submessageType = response.getSubmessageType();
			
			if(submessageType == 0){
				System.out.println("You have no outstanding messages in your message store");
			} else if(submessageType == 1){
				// TODO: Handle cases when there are multiple outstanding messages
			}
			break;
			
		case "delete":
			user.deleteUser();
			
			response = user.parseResponse();
			submessageType = response.getSubmessageType();
			
			if(submessageType == 0){
				System.out.println("User successfully deleted");
			} else if(submessageType == 1){
				System.out.println("You are not currently logged in.  No users have been deleted");
			}
			break;
			
		case "exit":
			user.exit();
			
			response = user.parseResponse();
			submessageType = response.getSubmessageType();
			if(submessageType == 0){
				System.out.println("Logout successful!");
			} else if(submessageType == 1){
				System.out.println("You are not currently logged in");
			} else if(submessageType == 2){
				System.out.println("You have been logged out after 60 seconds due to inactivity");
			}
			
			user.isLoggedIn = false;
			loggedIn = false;
			break;
			
		default:
			System.out.println("Unrecognized command: "+command);
			System.out.println(
				"Usage: \n"+
				"echo : Server returns the same text sent to it\n"+
				"send : Send a message to an existing user\n"+
				"query: Retrieve messages that have been sent to the user currently logged in\n"+
				"delete: Deletes the user that is currently logged in and logs the user out\n"+
				"exit : Logs off the current user\n");
			break;
		}
	}
}
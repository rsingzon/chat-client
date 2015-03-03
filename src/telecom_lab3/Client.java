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
	
	public static void main(String args[]){		

		String ip = null;
		int port = 5000;
		boolean loggedIn = false;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try{
			InetAddress address = InetAddress.getByName("ecse-489.ece.mcgill.ca");
			ip = address.getHostAddress();
		} catch(UnknownHostException e){
			System.out.println("Unknown host: " + e.getStackTrace());
		}
		
		while(true){
			String username = null;
			String password = null;
			
			// If logged out, prompt the user for username and password
			if(!loggedIn){
				try{
					System.out.println("Username");
					username = reader.readLine();
					System.out.println("Password");
					password = reader.readLine();
				} catch (IOException e){
					e.printStackTrace();
				}
				
				// Create a user object
				User user = new User(username, password, ip, port);
				
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
	}
	
	/**
	 * Parses the user input and calls the functions to perform the command
	 * @param command
	 * @return
	 */
	private static void parseCommand(String command){
		
		String commandLowercase = command.toLowerCase();
		
		switch(commandLowercase){
		case "exit":
			System.exit(0);
			break;
		default:
			System.out.println("Unrecognized command: "+command);
			break;
		}
	}
}
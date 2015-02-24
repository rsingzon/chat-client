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
import java.net.Socket;
import java.net.UnknownHostException;

public class Lab3 { 
	
	public static void main(String args[]){		
	
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		while(true){
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
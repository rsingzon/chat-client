/**
 * User class - defines the functions available for any particular user
 */

package telecom_lab3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class User {
	boolean isLoggedIn = false;
	private String username;
	private String password;
	private Socket socket;
	private String dummyString = "dummy";

	/**
	 * Constructor for a single user
	 * @param name
	 * @param pw
	 */
	public User(String name, String pw, Socket s) {
		this.username = name;
		this.password = pw;
		this.socket = s;
	}

	/**
	 * Sends a formatted message to the server using the socket opened in the client
	 * @param socket
	 * @param message
	 */
	public void sendMessage(Message message) {
	
		try{
			// Write message to output stream
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			output.writeBytes(new String(message.getBytes())); // UTF is a string encoding	
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Exits the program
	 */
	public void exit() {
		Message message = new Message(Operation.getValue(Operation.EXIT), 0, dummyString.length(), dummyString);
		sendMessage(message);
	}
	
	/**
	 * Sends a message to the server, and the server will respond with the same message
	 * @param echoText
	 */
	public void echo(String echoText) {
		int size = echoText.getBytes().length;
		Message message = new Message(Operation.getValue(Operation.ECHO), 0, size, echoText);
		sendMessage(message);
	}

	/**
	 * Logs a user in
	 * @param username
	 * @param password
	 * @return boolean indicating whether the username is valid and the login command was sent to the server
	 */
	public boolean login() {
		
		// Check that the username is valid (ie. there are no commas)
		if(username.contains(",")){
			System.out.println("Badly formatted username");
			return false;
		}
		
		String data = username + "," + password;
		int dataSize = data.getBytes().length;

		Message message = new Message(Operation.getValue(Operation.LOGIN), 0, dataSize, data);
		sendMessage(message);
		return true;
	}
	
	/**
	 * Logs a user out
	 */
	public void logoff() {
		Message message = new Message(Operation.getValue(Operation.LOGOFF), 0, dummyString.length(), dummyString);
		sendMessage(message);
	}

	/**
	 * Creates a new user
	 * @param username
	 * @param password
	 * @return boolean indicating whether the username is valid and the create command was sent to the server
	 */
	public boolean createUser() {
		
		// Check if the username contains commas
		if(username.contains(",")){
			System.out.println("Badly formatted username");
			return false;
		}
		
		String data = username + "," + password;
		int dataSize = data.getBytes().length;
		
		Message message = new Message(Operation.getValue(Operation.CREATE_USER), 0, dataSize, data);
		sendMessage(message);
		return true;
	}
	
	/**
	 * Deletes the user that is currently logged in
	 */
	public void deleteUser() {
		Message message = new Message(Operation.getValue(Operation.DELETE_USER), 0, dummyString.length(), dummyString);
		sendMessage(message);
	}

	/**
	 * Creates a store for the currently logged in user
	 */
	public void createStore() {
		
		Message message = new Message(Operation.getValue(Operation.CREATE_STORE), 0, dummyString.length(), dummyString);
		sendMessage(message);
	}
	
	/**
	 * Sends a message to the specified user
	 * @param destinationUser
	 * @param message
	 */
	public void sendMessageToUser(String destinationUser, String message) {
		String data = destinationUser + "," + message;
		int dataSize = data.getBytes().length;
		
		Message serverMessage = new Message(Operation.getValue(Operation.SEND_MESSAGE_TO_USER), 0, dataSize, data);
		sendMessage(serverMessage);
	}

	/**
	 * Queries the server for new messages
	 */
	public void queryMessages() {
		Message message = new Message(Operation.getValue(Operation.QUERY_MESSAGES), 0, dummyString.length(), dummyString);
		sendMessage(message);
	}
	
	/**
	 * Takes messages obtained from a user's store and returns a string in a more readable format
	 * Messages will be in the form:
	 * from_user,2015-03-09 10:14:03,message
	 * 
	 * The output will be of the form:
	 * [2015-03-09 10:14:03] from_user >> to_user: message 
	 * @param message
	 * @return formatted message
	 */
	public String formatMessage(String message){
		//Split the message into three parts, the sender, timestamp, and message string
		String parts[] = message.split(",", 3);
		return "[" + parts[1] + "] " + parts[0] + " >> " + username + ": " + parts[2];
	}
	
	/**
	 * Parses the response from the server and informs user of status
	 * @return submessage type
	 */
	public Message parseResponse(){

		byte[] readBuf = new byte[1000];
		int bytesReceived = 0;
		int bytes = 0;

		
		try{
			// Wait for login success message from server
			DataInputStream input = new DataInputStream(socket.getInputStream());

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
			
//			System.out.println("Response: ");
//			System.out.println("Type: "+ messageType);
//			System.out.println("Submessage: "+submessageType);
//			System.out.println("Size: "+dataSize);
//			System.out.println("Data: "+data);
			return new Message(messageType, submessageType, dataSize, data);
			
		} catch (IOException e){
			System.out.println("IO Exception on parsing server response: " + e.getMessage());
		}

		return null;
	}
}

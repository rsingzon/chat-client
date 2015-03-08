/**
 * User class - defines the functions available for any particular user
 */

package telecom_lab3;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class User {
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
	public void sendMessage(Socket socket, Message message) {
	
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
		sendMessage(socket, message);
	}
	
	/**
	 * Sends a message to the server, and the server will respond with the same message
	 * @param echoText
	 */
	public void echo(String echoText) {
		int size = echoText.getBytes().length;
		Message message = new Message(Operation.getValue(Operation.ECHO), 0, size, echoText);
		sendMessage(socket, message);
	}

	/**
	 * Logs a user in
	 * @param username
	 * @param password
	 */
	public void login(String username, String password) {
		String data = username + "," + password;
		int dataSize = data.getBytes().length;

		Message message = new Message(Operation.getValue(Operation.LOGIN), 0, dataSize, data);
		sendMessage(socket, message);
	}
	
	/**
	 * Logs a user out
	 */
	public void logoff() {
		Message message = new Message(Operation.getValue(Operation.LOGOFF), 0, dummyString.length(), dummyString);
		sendMessage(socket, message);
	}

	/**
	 * Creates a new user
	 * @param username
	 * @param password
	 */
	public void createUser(String username, String password) {
		String data = username + "," + password;
		int dataSize = data.getBytes().length;
		
		Message message = new Message(Operation.getValue(Operation.CREATE_USER), 0, dataSize, data);
		sendMessage(socket, message);
	}
	
	/**
	 * Deletes the user that is currently logged in
	 */
	public void deleteUser() {
		Message message = new Message(Operation.getValue(Operation.DELETE_USER), 0, dummyString.length(), dummyString);
		sendMessage(socket, message);
	}

	/**
	 * Creates a store for the currently logged in user
	 */
	public void createStore() {
		
		Message message = new Message(Operation.getValue(Operation.CREATE_STORE), 0, dummyString.length(), dummyString);
		sendMessage(socket, message);
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
		sendMessage(socket, serverMessage);
	}

	/**
	 * Queries the server for new messages
	 */
	public void queryMessages() {
		Message message = new Message(Operation.getValue(Operation.QUERY_MESSAGES), 0, dummyString.length(), dummyString);
		sendMessage(socket, message);
	}
}

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

	/**
	 * Constructor for a single user
	 * 
	 * @param name
	 * @param pw
	 */
	public User(String name, String pw, Socket s) {
		this.username = name;
		this.password = pw;
		this.socket = s;
	}

	public void sendMessage(Socket socket, Message message) {
		
		try{
			// Write message to output stream
			System.out.println("Sending...");
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			output.writeBytes(new String(message.getBytes())); // UTF is a string encoding	
		} catch(IOException e){
			
		}
	}
	
	public void exit() {
		Message message = new Message(Operation.getValue(Operation.EXIT), 0, 0, "");
		sendMessage(socket, message);
	}
	
	public void echo(String echoText) {
		int size = echoText.getBytes().length;
		Message message = new Message(Operation.getValue(Operation.ECHO), 0, size, echoText);
		sendMessage(socket, message);
	}

	public void login(String username, String password) {
		String data = username + "," + password;
		int dataSize = data.getBytes().length;

		Message message = new Message(Operation.getValue(Operation.LOGIN), 0, dataSize, data);
		sendMessage(socket, message);
	}
	
	public void logoff() {
		Message message = new Message(Operation.getValue(Operation.LOGOFF), 0, 0, "");
		sendMessage(socket, message);
	}

	public void createUser(String username, String password) {
		String data = username + "," + password;
		int dataSize = data.getBytes().length;
		
		Message message = new Message(Operation.getValue(Operation.CREATE_USER), 0, dataSize, data);
		sendMessage(socket, message);
		
		// Create store once per user
		createStore();
	}
	
	public void deleteUser() {
		Message message = new Message(Operation.getValue(Operation.DELETE_USER), 0, 0, "");
		sendMessage(socket, message);
	}

	public void createStore() {
		Message message = new Message(Operation.getValue(Operation.CREATE_STORE), 0, 0, "");
		sendMessage(socket, message);
	}
	
	public void sendMessageToUser(String destinationUser, String message) {
		String data = destinationUser + "," + message;
		int dataSize = data.getBytes().length;
		
		Message serverMessage = new Message(Operation.getValue(Operation.SEND_MESSAGE_TO_USER), 0, dataSize, data);
		sendMessage(socket, serverMessage);
	}

	public void queryMessages() {
		Message message = new Message(Operation.getValue(Operation.QUERY_MESSAGES), 0, 0, "");
		sendMessage(socket, message);
	}
}

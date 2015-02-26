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
	public User(String name, String pw, String ip, int port) {
		this.username = name;
		this.password = pw;

		try {
			// Connect to the server
			socket = new Socket(ip, port);

			// Check if the user exists
			InetAddress address = InetAddress.getByName("www.google.ca");
			System.out.println(address.getHostAddress());

			login(username, password);

			// Create a store
			createStore();

		}

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

	public void sendMessage(Socket socket, Message message) {
		
		try{
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			// Send length
			System.out.println("Length" + message.getSize());
			output.writeInt(message.getSize());
			
			// Send data
			System.out.println("Sending...");
			output.writeBytes(new String(message.getBytes())); // UTF is a string encoding	
		} catch(IOException e){
			
		}
	}
	
	public void exit() {
		Message message = new Message(20, 0, 0, "");
		sendMessage(socket, message);
	}
	
	public void echo(String echoText) {
		int size = echoText.getBytes().length;
		Message message = new Message(22, 0, size, echoText);
		sendMessage(socket, message);
	}

	public void login(String username, String password) {
		String data = username + "," + password;
		int dataSize = data.getBytes().length;

		Message message = new Message(23, 0, dataSize, data);
		sendMessage(socket, message);
	}
	
	public void logoff() {
		Message message = new Message(24, 0, 0, "");
		sendMessage(socket, message);
	}

	public void createUser(String username, String password) {
		String data = username + "," + password;
		int dataSize = data.getBytes().length;
		
		Message message = new Message(25, 0, dataSize, data);
		sendMessage(socket, message);
	}
	
	public void deleteUser() {
		Message message = new Message(26, 0, 0, "");
		sendMessage(socket, message);
	}

	public void createStore() {
		Message message = new Message(27, 0, 0, "");
		sendMessage(socket, message);
	}
	
	public void sendMessageToUser(String destinationUser, String message) {
		String data = destinationUser + "," + message;
		int dataSize = data.getBytes().length;
		
		Message serverMessage = new Message(28, 0, dataSize, data);
		sendMessage(socket, serverMessage);
	}

	public void queryMessages() {
		Message message = new Message(29, 0, 0, "");
		sendMessage(socket, message);
	}
}

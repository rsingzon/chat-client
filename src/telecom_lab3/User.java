package telecom_lab3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

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

	public void createStore() {

	}

	public void exit() {

	}

	public void login(String username, String password) {
		String data = username + "," + password;
		int dataSize = data.getBytes().length;

		Message message = new Message(23, 0, dataSize, data);
		sendMessage(socket, message);
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

	public void deleteUser() {

	}

	public void queryMessages() {

	}
}

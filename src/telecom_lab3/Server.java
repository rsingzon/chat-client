/**
 * ECSE 489 - Telecommunication Networks Lab
 * Winter 2015
 * Professor Coates
 * 
 * @author Singzon, Ryan			260397455
 * @author Muralidharan, Keertana	260409960
 */

package telecom_lab3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Hashtable;

public class Server {

	private static DataInputStream input;
	private static DataOutputStream output;
	private static Socket clientSocket;
	private Hashtable<String, User> connectedUsers;

	public static void main(String args[]) {
		try {
			int serverPort = 8080;
			ServerSocket listenSocket = new ServerSocket(serverPort);

			System.out.println("Server listening...");

			// Continuously receive messages from connecting clients
			while (true) {
				clientSocket = listenSocket.accept();

				try {
					input = new DataInputStream(clientSocket.getInputStream());
					output = new DataOutputStream(clientSocket.getOutputStream());

					// Step 1: Read the number of bytes received
					int numBytes = input.readInt();
					System.out.println("Read Length: " + numBytes);

					// Step 2: Read the message
					System.out.println("Reading...");

					byte[] readBuf = new byte[1000];
					int bytesReceived = 0;
					int bytes = 0;

					// Copy bytes into buffer
					while (bytesReceived < numBytes) {
						bytes = input.read(readBuf);
						bytesReceived += bytes;
					}

					// Extract the message from the byte array
					byte[] typeBytes = Arrays.copyOfRange(readBuf, 0, 4);
					byte[] submessageTypeBytes = Arrays.copyOfRange(readBuf, 4,
							8);
					byte[] sizeBytes = Arrays.copyOfRange(readBuf, 8, 12);

					int messageType = ByteBuffer.wrap(typeBytes).getInt();
					int submessageType = ByteBuffer.wrap(submessageTypeBytes)
							.getInt();
					int dataSize = ByteBuffer.wrap(sizeBytes).getInt();

					byte[] dataBytes = Arrays.copyOfRange(readBuf, 12,
							12 + dataSize);
					String data = new String(dataBytes);

					// Print message information
					System.out.println("Message Type: " + messageType);
					System.out.println("Submessage Type: " + submessageType);
					System.out.println("Data size: " + dataSize);
					System.out.println("Data: " + data);

					// Parse the command
					parseCommand(messageType, dataSize, data);

					System.out.println("\nMessage received\n");

				} catch (IOException e) {
					System.out.println("Connection:" + e.getMessage());
				}
			}
		}

		catch (IOException e) {
			System.out.println("Listen :" + e.getMessage());
		}
	}

	public static void parseCommand(int messageType, int dataSize, String data) {

		Operation operation = Operation.getOperation(messageType);
		
		switch (operation) {
		// Exit
		case EXIT:

			// Closes current connection and logs the user out
			break;

		// Badly formatted message
		case BADLY_FORMATTED_MESSAGE:

			break;

		// Echo
		case ECHO:

			break;

		// Login
		case LOGIN:
			login(data);
			break;

		// Logoff
		case LOGOFF:

			break;

		// Create user
		case CREATE_USER:

			break;

		// Delete user
		case DELETE_USER:

			break;

		// Create store
		case CREATE_STORE:

			break;

		// Send message to user
		case SEND_MESSAGE_TO_USER:

			break;

		// Query messages
		case QUERY_MESSAGES:

			break;

		default:
			System.out.println("Unrecognized message type" + messageType);
			break;
		}

	}
	
	public static void login(String data) {
		
		// Verify that the message is correctly formatted
		if(!data.contains(",")) {
			//Message message = new Message();
		}
			
	}
}

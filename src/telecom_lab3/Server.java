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

public class Server {
	public static void main(String args[]) {
		try {
			int serverPort = 8080;
			ServerSocket listenSocket = new ServerSocket(serverPort);

			System.out.println("Server listening...");

			while (true) {
				Socket clientSocket = listenSocket.accept();
				Connection c = new Connection(clientSocket);
			}
		} 
		
		catch (IOException e) {
			System.out.println("Listen :" + e.getMessage());
		}
	}
}


/**
 * This class handles client requests
 *
 */
class Connection extends Thread {
	DataInputStream input;
	DataOutputStream output;
	Socket clientSocket;

	public Connection(Socket aClientSocket) {
		try {
			this.clientSocket = aClientSocket;
			this.input = new DataInputStream(clientSocket.getInputStream());
			this.output = new DataOutputStream(clientSocket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.out.println("Connection:" + e.getMessage());
		}
	}

	public void run() {

		try {
			  // Step 1: Read the number of bytes received
			  int numBytes = input.readInt();
			  System.out.println("Read Length: "+ numBytes);
			  
			  // Step 2: Read the message
			  System.out.println("Reading...");
			   
			  byte[] readBuf = new byte[1000];
			  int bytesReceived = 0;
			  int bytes = 0;
			  while(bytesReceived < numBytes){
				  bytes = input.read(readBuf);
				  //System.out.println(new String(readBuf));
				  bytesReceived += bytes;
			  }
			  
			  // Extract the message from the byte array
			  byte[] typeBytes = Arrays.copyOfRange(readBuf, 0, 4);
			  byte[] submessageTypeBytes = Arrays.copyOfRange(readBuf, 4, 8);
			  byte[] sizeBytes = Arrays.copyOfRange(readBuf, 8, 12);
			  
			  int messageType = ByteBuffer.wrap(typeBytes).getInt();
			  int submessageType = ByteBuffer.wrap(submessageTypeBytes).getInt();
			  int dataSize = ByteBuffer.wrap(sizeBytes).getInt();
			  
			  byte[] dataBytes = Arrays.copyOfRange(readBuf, 12, 12+dataSize);
			  String data = new String(dataBytes);
			  
			  System.out.println("Message Type: "+messageType);
			  System.out.println("Submessage Type: "+submessageType);
			  System.out.println("Data size: "+dataSize);
			  System.out.println("Data: "+data);
			  
			  System.out.println("Message received");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		finally {
			try {
				clientSocket.close();
			} catch (IOException e) {/* close failed */
			}
		}
	}
}

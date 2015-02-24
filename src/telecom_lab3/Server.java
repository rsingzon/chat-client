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
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String args[]) {
		try {
			int serverPort = 8080;
			ServerSocket listenSocket = new ServerSocket(serverPort);

			System.out.println("server start listening... ... ...");

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

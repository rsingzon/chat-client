package telecom_lab3;

import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.*;

import org.junit.*;

import junit.framework.TestCase;

public class ClientTest extends TestCase {
	InetAddress address;
	String ip; 
	int port = 5001;

	public void testUserExists() throws Exception {
		try {
			address = InetAddress.getByName("ecse-489.ece.mcgill.ca");
			ip = address.getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + e.getMessage());
		}
		Socket socket = new Socket(ip, port);
		User user = new User("ryan", "singzon", socket);
		user.createUser();
		// assuming ryan already exists
		
		Message message = user.parseResponse();
		assertEquals("User exists", 1, message.getSubmessageType());
		socket.close();

	}
	
	public void testUserAlreadyLoggedIn() throws Exception {
		try {
			address = InetAddress.getByName("ecse-489.ece.mcgill.ca");
			ip = address.getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + e.getMessage());
		}
		Socket socket = new Socket(ip, port);
		
		// assuming ryan already exists
		
		User user= new User("ryan","singzon",socket);
		user.login();
		User user1 = new User("keertana", "murali", socket);
		user.createUser();
		user.parseResponse();
		Message message = user.parseResponse();
		assertEquals("User already logged in", 2, message.getSubmessageType());
		socket.close();
	

	}
	
	public void testdelete() throws Exception {
		try {
			address = InetAddress.getByName("ecse-489.ece.mcgill.ca");
			ip = address.getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + e.getMessage());
		}
		Socket socket = new Socket(ip, port);
		
		// assuming ryan already exists
		
		User user= new User("ryan","singzon",socket);
		user.deleteUser();
		Message message = user.parseResponse();
		assertEquals("User not logged in", 1, message.getSubmessageType());
		socket.close();
		
	}
	
	public void teststorenotloggedin() throws Exception {
		try {
			address = InetAddress.getByName("ecse-489.ece.mcgill.ca");
			ip = address.getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + e.getMessage());
		}
		Socket socket = new Socket(ip, port);
		
		// assuming ryan already exists
		
		User user= new User("ryan","singzon",socket);
		user.createStore();
		Message message = user.parseResponse();
		assertEquals("User not logged in", 2, message.getSubmessageType());
		socket.close();
		

	}
}

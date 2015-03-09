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
	
	@Before
	public void setup() {
		
	}

	

	@Test
	public void testUserExists() throws Exception {
		try {
			address = InetAddress.getByName("ecse-489.ece.mcgill.ca");
			ip = address.getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + e.getMessage());
		}
		Socket socket = new Socket(ip, port);
		User user = new User("ryan", "singzon", socket);
		user.createUser("ryan", "singzon");
		// assuming ryan already exists
		
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
			
			System.out.println("Response: ");
			System.out.println("Type: "+ messageType);
			System.out.println("Submessage: "+submessageType);
			System.out.println("Size: "+dataSize);
			System.out.println("Data: "+data);
			Message response = new Message(messageType, submessageType, dataSize, data);
			assertEquals("user already exists", 1, submessageType);
		}
		catch(Exception e){
			
		}
		
		
		socket.close();

	}
}

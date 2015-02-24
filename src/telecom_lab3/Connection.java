/**
 * ECSE 489 - Telecommunication Networks Lab
 * Winter 2015
 * Professor Coates
 * 
 * The connection class creates a link between two devices
 * using sockets
 * 
 * @author Singzon, Ryan			260397455
 * @author Muralidharan, Keertana	260409960
 */

package telecom_lab3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {

	int port;
	String ip;
	Socket socket;
	
	/**
	 * 
	 * @param i
	 * @param p
	 */
	public Connection(String i, int p){
		this.port = p;
		this.ip = i;
		try{
			socket = new Socket(ip, p);
		} catch(Exception e){
			System.out.println();
		}
	}
	
	/**
	 * This method sends a file to the client.
	 * @param MsgLen
	 * @return
	 */
	public boolean sendMessage(String data) {
		Socket s = null; 
		try{ 
			DataInputStream input = new DataInputStream( s.getInputStream()); 
			DataOutputStream output = new DataOutputStream( s.getOutputStream()); 
		
			//Step 1 send length
			System.out.println("Length"+ data.length());
			output.writeInt(data.length());
			  
			//Step 2 send data
			System.out.println("Sending.......");
			output.writeBytes(data); // UTF is a string encoding
			  
			//Step 1 read length
			int nb = input.readInt();
			if(nb == data.length()){
				System.out.println("Confirmation Received");
				return true;
			}
			else{
				return false;
			}
		}
		catch (UnknownHostException e){ 
			System.out.println("Sock:"+e.getMessage());
		}
		catch (EOFException e){
			System.out.println("EOF:"+e.getMessage()); 
		}
		catch (IOException e){
			System.out.println("IO:"+e.getMessage());
		} 
		
		return false;
	}
}

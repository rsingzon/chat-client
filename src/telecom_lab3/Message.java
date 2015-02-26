package telecom_lab3;

import java.nio.ByteBuffer;

public class Message {

	private int messageType;
	private int submessageType;
	private int size;
	private byte[] data;
	private byte[] messageInBytes;
	
	public Message(int mt, int smt, int s, String d) {
		this.messageType = mt;
		this.submessageType = smt;
		this.data = d.getBytes();		// If this doesn't work, try changing the charset
		this.size = data.length;
		
		// Insert the message parameters into a byte array
		messageInBytes = new byte[12 + size];
		
		byte[] messageType_bytes = ByteBuffer.allocate(4).putInt(messageType).array();
		byte[] submessageType_bytes = ByteBuffer.allocate(4).putInt(submessageType).array();
		byte[] size_bytes = ByteBuffer.allocate(4).putInt(size).array();
		
		System.arraycopy(messageType_bytes, 0, messageInBytes, 0, 4);
		System.arraycopy(submessageType_bytes,0,messageInBytes,4,4);
		System.arraycopy(size_bytes,0,messageInBytes,8,4);
		System.arraycopy(data,0,messageInBytes,12,data.length);
	}
	
	public int getMessageType(){
		return messageType;
	}
	
	public int getSubmessageType(){
		return submessageType;
	}
	
	public int getSize(){
		return size;
	}
	
	public String getDataString(){
		return new String(data);
	}
	
	public byte[] getDataBytes(){
		return data;
	}
	
	public byte[] getBytes(){
		return messageInBytes;
	}
}

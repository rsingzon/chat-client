package telecom_lab3;

public class Message {

	private int messageType;
	private int submessageType;
	private int size;
	private byte[] data;
	
	public Message(int mt, int smt, int s, String d){
		this.messageType = mt;
		this.submessageType = smt;
		this.data = d.getBytes();
		// If this doesn't work, try changing the charset
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
	
	public String getData(){
		return new String(data);
	}
}

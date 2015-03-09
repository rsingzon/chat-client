package telecom_lab3;

import java.util.ArrayList;
 
/**
 * Starts a thread to continuously poll the server for new messages when a user is logged in 
 */
public class QueryThread extends Thread {
 
	User user;
	
	/**
	 * Default constructor - starts thread
	 */
    public QueryThread(User u) {
    	user = u;
    }
 
    @Override
    public void run() {
        ArrayList<Message> newMessages = new ArrayList<Message>();
        Message response;
        int submessageType;
        
        while (user.isLoggedIn){
        	try{
        		Thread.sleep(1000);
        
        		user.queryMessages();
        		response = user.parseResponse();
        		
        		submessageType = response.getSubmessageType();
        		// No messages available
        		if(submessageType == 0){
    			} 
        		
        		// There are messages available
        		else if(submessageType == 1){
    				System.out.println("Messages available!");
    				System.out.println(response.getDataString());
    			}
        		
        	} catch(InterruptedException e){
        		System.out.println("Query messages interrupted. " + e.getMessage());
        	}
        }
    }
}

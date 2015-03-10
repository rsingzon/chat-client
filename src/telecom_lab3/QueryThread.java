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
        
        while (user.isLoggedIn){
        	try{
        		
        		// Wait 1 second between querying the server
        		Thread.sleep(1000);
        
        		// Query for messages and parse the response
        		user.queryMessages();
        		newMessages = user.parseQuery();
        		
        		// Iterate through all the received messages and print them
        		for(Message message : newMessages){
        			if(message.getSubmessageType() == 1){
        				System.out.println(user.formatMessage(message.getDataString()));
        			}
        		}
        	} catch(InterruptedException e){
        		System.out.println("Query messages interrupted. " + e.getMessage());
        	}
        }
    }
}

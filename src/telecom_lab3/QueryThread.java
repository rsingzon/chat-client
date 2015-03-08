package telecom_lab3;

import java.io.IOException;
import java.util.ArrayList;
 
/**
 * 
 */
public class QueryThread extends Thread {
 
    public QueryThread() {
    }
 
    @Override
    public void run() {
    	pollMessages();
    }
 
    private void pollMessages() {
        boolean isRunning = true;
        ArrayList<Message> newMessages = new ArrayList<Message>();
 
        while (isRunning){
        	try{
        		Thread.sleep(1000);
        	} catch(InterruptedException e){
        		System.out.println("Query messages interrupted. " + e.getMessage());
        	}
        }
    }
}

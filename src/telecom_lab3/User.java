package telecom_lab3;

import java.util.ArrayList;

public class User {
	private ArrayList<String> messages;
	private String username;
	private String password;
	
	/**
	 * Constructor for a single user
	 * @param name
	 * @param pw
	 */
	public User(String name, String pw){
		this.username = name;
		this.password = pw;
		createStore();
	}
	
	
	public void createStore(){
		
		
	}
	
	public void retrieveMessages(){
		
	}
}

package telecom_lab3;

public enum Operation {
    EXIT,
    BADLY_FORMATTED_MESSAGE,
    ECHO, 
    LOGIN, 
    LOGOFF, 
    CREATE_USER, 
    DELETE_USER, 
    CREATE_STORE, 
    SEND_MESSAGE_TO_USER, 
    QUERY_MESSAGES;
    
    public static int getValue(Operation op){
    	switch(op){
    	case EXIT:
    		return 20;
    	case BADLY_FORMATTED_MESSAGE:
    		return 21;
    	case ECHO:
    		return 22;
    	case LOGIN:
    		return 23;
    	case LOGOFF:
    		return 24;
    	case CREATE_USER:
    		return 25;
    	case DELETE_USER:
    		return 26;
    	case CREATE_STORE:
    		return 27;
    	case SEND_MESSAGE_TO_USER:
    		return 28;
    	case QUERY_MESSAGES:
    		return 29;
    	default:
    		return 0;
    	}
    }
}

package redhawk.websocket.test.util;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class RedhawkWebSocketTestUtil extends WebSocketAdapter{
	private StringBuilder data = new StringBuilder(); 
	
	private Integer messagesToKeep = 100;
	
	private Integer messageCount = 0; 
	
	private Boolean resetAtMessagesToKeep = false;
	
	public RedhawkWebSocketTestUtil(Integer messagesToKeep){
		this.messagesToKeep = messagesToKeep;
	}
	
	@Override
    public void onWebSocketConnect(Session sess)
    {
        super.onWebSocketConnect(sess);
        data.append("socket connected: " + sess+"\n");
    }
    
    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);
        if(messagesToKeep>messageCount){
        	data.append("received text message: " + message+"\n");
        	messageCount++;
        }else if(messagesToKeep>messageCount && resetAtMessagesToKeep){
        	messageCount=0;
        	data = new StringBuilder();
        }
        	
    }
    
    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len)
    {
    	super.onWebSocketBinary(payload, offset, len);
        if(messagesToKeep>messageCount){
        	data.append("received binary message...\n");
        	messageCount++;        	
        }else if(messagesToKeep>messageCount && resetAtMessagesToKeep){
        	messageCount=0;
        	data = new StringBuilder();
        }
    }

    
    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode,reason);
        data.append("socket disconnected: [" + statusCode + "] " + reason+"\n");
    }
    
    @Override
    public void onWebSocketError(Throwable cause)
    {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
    
    public String getData(){
    	return data.toString();
    }
    
    public Integer getMessageCount(){
    	return messageCount;
    }
    
    public Integer getMessagesToKeep(){
    	return messagesToKeep;
    }
}

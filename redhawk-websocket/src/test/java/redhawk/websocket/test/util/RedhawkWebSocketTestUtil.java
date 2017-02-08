/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
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

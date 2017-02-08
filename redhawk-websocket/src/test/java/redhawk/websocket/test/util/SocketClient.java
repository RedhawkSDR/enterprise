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

import java.net.URI;
import java.util.concurrent.Future;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

/**
 *Java App for hitting websocket endpoints.  
 */
public class SocketClient {
	 public static void main(String[] args)
	    {
	        URI uri = URI.create("ws://localhost:8181/redhawk/localhost:2809/domains/REDHAWK_DEV/applications/WebsocketTest/components/SigGen.*/ports/out");

	        WebSocketClient client = new WebSocketClient();
	        try
	        {
	            try
	            {
	                client.start();
	                // The socket that receives events
	                RedhawkWebSocketTestUtil socket = new RedhawkWebSocketTestUtil(3);
	                // Attempt Connect
	                Future<Session> fut = client.connect(socket,uri);
	                //while(socket.getMessageCount()<3){
	                //	System.out.println("Messages to keep: "+socket.getMessageCount());
	                //}
	                
	                //client.stop();
	                Session session = fut.get();
	                session.getRemote().sendString("listAvailableProcessors");
	                Thread.sleep(10000l);
	                System.out.println(socket.getData());
	                // Wait for Connect
	                // Send a message
	                //session.getRemote().sendString("Hello");
	                // Close session
	            }
	            finally
	            {
	                client.stop();
	            }
	        }
	        catch (Throwable t)
	        {
	            t.printStackTrace(System.err);
	        }
	    }
}

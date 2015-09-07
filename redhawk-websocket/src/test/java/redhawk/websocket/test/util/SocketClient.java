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

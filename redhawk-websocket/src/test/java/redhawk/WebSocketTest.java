package redhawk;

import javax.websocket.DeploymentException;

import org.junit.Ignore;

@Ignore("This is also not in proper unit test setup...")
public class WebSocketTest {

	public static void main(String[] args) throws DeploymentException {

//        Server server = new Server("localhost", 8025, "/redhawk/websocket", EventChannelEndpoint.class, DevicePortEndpoint.class, ComponentPortEndpoint.class, ShortHandComponentPortEndpoint.class);
//        while(true) {
//        	server.start();
//        }

		Object o = null;
		
		Long s = (Long) o;
		
		if(s == null) {
			System.out.println("YUP");
		}
		
		System.out.println(s);
		
	}
	
}

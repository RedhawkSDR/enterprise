package redhawk.rest.endpoints;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class RedhawkResourceTestBase {
	static Server server;
	
	static Client client; 
	
	static String baseUri = "http://localhost:8080/redhawk/";
	
	static String domainName = "REDHAWK_DEV";
	
	@BeforeClass
	public static void setup() throws Exception{
		server = new Server(8080);
		WebAppContext webapp = new WebAppContext();
		webapp.setResourceBase("src/test/resources/webapp");
		server.setHandler(webapp);
		System.out.println("Starting embedded Jetty");
		server.start();

		client = ClientBuilder.newBuilder().newClient();
	}
	
	@AfterClass
	public static void tearDown() throws Exception{
		System.out.println("Stopping embedded Jetty");
		server.stop();
	}
}


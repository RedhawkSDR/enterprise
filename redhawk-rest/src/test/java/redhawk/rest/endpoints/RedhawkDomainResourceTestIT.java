package redhawk.rest.endpoints;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RedhawkDomainResourceTestIT {
	//TODO: Check message body

	private static Server server;
	
	private static Client client; 
	
	private String baseUri = "http://localhost:8080/redhawk/";
	
	private String domainName = "REDHAWK_DEV";
	
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
	
	@Test
	public void testGetDomains() throws InterruptedException{
		WebTarget target = client.target(baseUri+"/localhost:2809/domains");
		//System.out.println(target.request().get().readEntity(String.class));
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());		
	}
	
	@Test
	public void testGetDomain() throws InterruptedException{
		WebTarget target = client.target(baseUri+"/localhost:2809/domains/"+domainName);
		//System.out.println(target.request().get().readEntity(String.class));
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());		
	}
	
	@Test
	public void testGetDomainProperties(){
		WebTarget target = client.target(baseUri+"/localhost:2809/domains/"+domainName+"/properties");
		//System.out.println(target.request().get().readEntity(String.class));
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());
		
		//TODO: Dynamically get all the property Id's that are available and make
		//sure you can retrieve each
	}
	
	@AfterClass
	public static void tearDown() throws Exception{
		System.out.println("Stopping embedded Jetty");
		server.stop();
	}
}

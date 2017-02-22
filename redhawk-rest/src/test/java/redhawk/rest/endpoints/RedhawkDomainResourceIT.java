package redhawk.rest.endpoints;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

public class RedhawkDomainResourceIT extends RedhawkResourceTestBase{
	//TODO: Check message body
	
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
}

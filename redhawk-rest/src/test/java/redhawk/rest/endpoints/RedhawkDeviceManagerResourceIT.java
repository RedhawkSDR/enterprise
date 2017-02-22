package redhawk.rest.endpoints;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import redhawk.rest.model.DeviceManager;
import redhawk.rest.model.DeviceManagerContainer;

public class RedhawkDeviceManagerResourceIT extends RedhawkResourceTestBase{	

	@Test
	public void testGetDeviceManagers(){
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/devicemanagers");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());		
	}
	
	@Test
	public void testGetDeviceManager(){		
		//Get Target From REST Endpoint
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/devicemanagers");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		DeviceManagerContainer container = response.readEntity(DeviceManagerContainer.class);
		assertEquals(200, response.getStatus());		

		//Hit Specific DeviceManager 
		target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/devicemanagers/"+container.getDeviceManagers().get(0).getLabel());
		response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());		
	}
}

package redhawk.rest.endpoints;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import redhawk.rest.model.Component;
import redhawk.rest.model.ComponentContainer;

public class RedhawkSoftwareComponentResourceIT extends RedhawkApplicationResourceTestBase{
	@Test
	public void testSoftwareComponents() throws InterruptedException{
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/components");
		System.out.println(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/components");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		ComponentContainer componentContainer = response.readEntity(ComponentContainer.class);
		assertEquals(200, response.getStatus());
		
		
		//Hit each component endpoint
		for(Component comp : componentContainer.getComponents()){
			target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/components/"+comp.getName()+"/softwarecomponent");
			response = target.request().accept(MediaType.APPLICATION_XML).get();
			assertEquals(200, response.getStatus());
		}
	}

}

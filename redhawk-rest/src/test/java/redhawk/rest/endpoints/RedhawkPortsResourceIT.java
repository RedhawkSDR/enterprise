package redhawk.rest.endpoints;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import redhawk.rest.model.Component;
import redhawk.rest.model.ComponentContainer;
import redhawk.rest.model.Port;
import redhawk.rest.model.PortContainer;

public class RedhawkPortsResourceIT extends RedhawkApplicationResourceTestBase{
	@Test
	public void testPorts() throws InterruptedException{
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/components");
		System.out.println(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/components");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		ComponentContainer componentContainer = response.readEntity(ComponentContainer.class);
		assertEquals(200, response.getStatus());
		
		Thread.sleep(30000l);
		
		//Hit each component endpoint
		for(Component comp : componentContainer.getComponents()){
			target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/components/"+comp.getName()+"/ports");
			response = target.request().accept(MediaType.APPLICATION_XML).get();
			PortContainer pContainer = response.readEntity(PortContainer.class);
			assertEquals(200, response.getStatus());
			
			//Get Each individual port and it's port statistics
			for(Port port : pContainer.getPorts()){
				target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/components/"+comp.getName()+"/ports/"+port.getName());
				response = target.request().accept(MediaType.APPLICATION_XML).get();
				Port restPort = response.readEntity(Port.class);
				assertEquals(200, response.getStatus());
				
				//Get the port statistics
				//TODO: Talk to team figure out if you should be able to getPortstatistics on a 'out'(i.e. dataFloat_out) port.
				if(port.getName().endsWith("in")){
					target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/components/"+comp.getName()+"/ports/"+port.getName()+"/statistics");
					response = target.request().accept(MediaType.APPLICATION_XML).get();
					assertEquals(200, response.getStatus());							
				}	
			}
		}
	}
}

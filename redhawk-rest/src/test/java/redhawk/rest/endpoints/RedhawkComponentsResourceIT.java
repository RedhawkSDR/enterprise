package redhawk.rest.endpoints;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import redhawk.rest.model.Component;
import redhawk.rest.model.ComponentContainer;
import redhawk.rest.model.Property;
import redhawk.rest.model.PropertyContainer;

public class RedhawkComponentsResourceIT extends RedhawkApplicationResourceTestBase{
	//private String baseUri = http://localhost:8181/cxf/redhawk/localhost:2809/domains/REDHAWK_DEV/applications/rh.basic.*/components;
	
	@Test
	public void testGetComponents() throws InterruptedException{
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/components");
		System.out.println(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/components");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		ComponentContainer componentContainer = response.readEntity(ComponentContainer.class);
		assertEquals(200, response.getStatus());
		
		
		//Hit each component endpoint
		for(Component comp : componentContainer.getComponents()){
			target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/components/"+comp.getName());
			response = target.request().accept(MediaType.APPLICATION_XML).get();
			assertEquals(200, response.getStatus());

			//Hit properties
			target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/components/"+comp.getName()+"/properties");
			response = target.request().accept(MediaType.APPLICATION_XML).get();
			PropertyContainer propContainer = response.readEntity(PropertyContainer.class);
			assertEquals(200, response.getStatus());

			//Hit each propertyId
			for(Property property : propContainer.getProperties()){
				target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/components/"+comp.getName()+"/properties/"+property.getId());
				response = target.request().accept(MediaType.APPLICATION_XML).get();
				assertEquals(200, response.getStatus());
			}
		}
	}
}

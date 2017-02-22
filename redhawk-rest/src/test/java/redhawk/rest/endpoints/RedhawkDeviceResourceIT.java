package redhawk.rest.endpoints;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.rest.model.Device;
import redhawk.rest.model.DeviceContainer;
import redhawk.rest.model.DeviceManager;
import redhawk.rest.model.DeviceManagerContainer;
import redhawk.rest.model.Property;
import redhawk.rest.model.PropertyContainer;

public class RedhawkDeviceResourceIT extends RedhawkResourceTestBase{
	private static String deviceManagerLabel; 
	
	@BeforeClass
	public static void setupDeviceManagerUri(){
		//Get Target From REST Endpoint
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/devicemanagers");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		DeviceManagerContainer container = response.readEntity(DeviceManagerContainer.class);
		deviceManagerLabel = container.getDeviceManagers().get(0).getLabel();
	}
	
	@Test
	public void testGetDevices(){
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/devicemanagers/"+deviceManagerLabel);
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		DeviceManager deviceManager = response.readEntity(DeviceManager.class);
		
		//Make sure 200 came back 
		assertEquals(200, response.getStatus());
		
		for(Device device : deviceManager.getDevices()){
			target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/devicemanagers/"+deviceManagerLabel+"/devices/"+device.getLabel());
			
			//Hits the device endpoint
			response = target.request().accept(MediaType.APPLICATION_XML).get();
			Device restDevice = response.readEntity(Device.class);
			assertEquals(200, response.getStatus());

			
			//Hit the properties endpoint
			target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/devicemanagers/"+deviceManagerLabel+"/devices/"+device.getLabel()+"/properties");
			response = target.request().accept(MediaType.APPLICATION_XML).get();
			PropertyContainer restProperties = response.readEntity(PropertyContainer.class);
			assertEquals(200, response.getStatus());
			
			//Hit all the properties for a device
			for(Property property : restProperties.getProperties()){
				target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/devicemanagers/"+deviceManagerLabel+"/devices/"+device.getLabel()+"/properties/"+property.getId());
				response = target.request().accept(MediaType.APPLICATION_XML).get();
				assertEquals(200, response.getStatus());
			}
		}
	}
}

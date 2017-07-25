/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package redhawk.rest.endpoints;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.rest.model.Device;
import redhawk.rest.model.DeviceManager;
import redhawk.rest.model.DeviceManagerContainer;
import redhawk.rest.model.Property;
import redhawk.rest.model.PropertyContainer;

public class RedhawkDeviceResourceIT extends RedhawkResourceTestBase{
	private static String deviceManagerLabel; 
	
	private static DeviceManager devMgr;
	
	@BeforeClass
	public static void setupDeviceManagerUri(){
		//Get Target From REST Endpoint
		WebTarget target = client.target(baseURI+"/"+domainName+"/devicemanagers");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		DeviceManagerContainer container = response.readEntity(DeviceManagerContainer.class);
		devMgr = container.getDeviceManagers().get(0);
		deviceManagerLabel = devMgr.getLabel();
	}
	
	@Test
	public void testGetDevices(){
		WebTarget target = client.target(baseURI+"/"+domainName+"/devicemanagers/"+deviceManagerLabel);
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		DeviceManager deviceManager = response.readEntity(DeviceManager.class);
		
		//Make sure 200 came back 
		assertEquals(200, response.getStatus());
		
		for(Device device : deviceManager.getDevices()){
			target = client.target(baseURI+"/"+domainName+"/devicemanagers/"+deviceManagerLabel+"/devices/"+device.getLabel());
			
			//Hits the device endpoint
			response = target.request().accept(MediaType.APPLICATION_XML).get();
			Device restDevice = response.readEntity(Device.class);
			assertEquals(200, response.getStatus());

			
			//Hit the properties endpoint
			target = client.target(baseURI+"/"+domainName+"/devicemanagers/"+deviceManagerLabel+"/devices/"+device.getLabel()+"/properties");
			response = target.request().accept(MediaType.APPLICATION_XML).get();
			PropertyContainer restProperties = response.readEntity(PropertyContainer.class);
			assertEquals(200, response.getStatus());
			
			//Hit all the properties for a device
			for(Property property : restProperties.getProperties()){
				target = client.target(baseURI+"/"+domainName+"/devicemanagers/"+deviceManagerLabel+"/devices/"+device.getLabel()+"/properties/"+property.getId());
				response = target.request().accept(MediaType.APPLICATION_XML).get();
				assertEquals(200, response.getStatus());
			}
		}
	}
	
	@Test
	public void testSetAdminState() {
		for(Device dev : devMgr.getDevices()) {
			String deviceBase = baseURI+"/"+domainName+"/devicemanagers/"+deviceManagerLabel+"/devices/"+dev.getLabel();
			WebClient adminClient = WebClient.create(deviceBase+"/adminstate");
			
			//Lock Device
			String state = "LOCKED";
			adminClient.type(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML);
			Response r = adminClient.put(state);			
			assertEquals(200, r.getStatus());
			
			WebTarget target = client.target(deviceBase);
			Response deviceResponse =  target.request().accept(MediaType.APPLICATION_XML).get();
			Device restDevice = deviceResponse.readEntity(Device.class);
			
			assertEquals(200, deviceResponse.getStatus());
			assertEquals(state, restDevice.getAdminState().toString());
			
			//Unlock Device
			state = "UNLOCKED";
			adminClient.type(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML);
			r = adminClient.put(state);
			
			deviceResponse =  target.request().accept(MediaType.APPLICATION_XML).get();
			restDevice = deviceResponse.readEntity(Device.class);
			
			assertEquals(200, deviceResponse.getStatus());
			assertEquals(state, restDevice.getAdminState().toString());
		}
	}
}

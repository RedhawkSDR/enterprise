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

import org.junit.Test;

import redhawk.rest.model.DeviceManagerContainer;

public class RedhawkDeviceManagerResourceIT extends RedhawkResourceTestBase{	

	@Test
	public void testGetDeviceManagers(){
		WebTarget target = client.target(baseURI+"/"+domainName+"/devicemanagers");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());		
	}
	
	@Test
	public void testGetDeviceManager(){		
		//Get Target From REST Endpoint
		WebTarget target = client.target(baseURI+"/"+domainName+"/devicemanagers");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		DeviceManagerContainer container = response.readEntity(DeviceManagerContainer.class);
		assertEquals(200, response.getStatus());		

		//Hit Specific DeviceManager 
		target = client.target(baseURI+"/"+domainName+"/devicemanagers/"+container.getDeviceManagers().get(0).getLabel());
		response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());		
	
		//Get a specific device managers properties
		target = client.target(baseURI+"/"+domainName+"/devicemanagers/"+container.getDeviceManagers().get(0).getLabel()+"/properties");
		response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());	
	}
}

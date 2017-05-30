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

import redhawk.rest.model.Component;
import redhawk.rest.model.ComponentContainer;
import redhawk.rest.model.Property;
import redhawk.rest.model.PropertyContainer;

public class RedhawkComponentsResourceIT extends RedhawkApplicationResourceTestBase{
	//private String baseUri = http://localhost:8181/cxf/redhawk/localhost:2809/domains/REDHAWK_DEV/applications/rh.basic.*/components;
	
	@Test
	public void testGetComponents() throws InterruptedException{
		WebTarget target = client.target(baseUri+"/"+domainName+"/applications/"+applicationName+"/components");
		//System.out.println(baseUri+"/domains/"+domainName+"/applications/"+applicationName+"/components");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		ComponentContainer componentContainer = response.readEntity(ComponentContainer.class);
		assertEquals(200, response.getStatus());
		
		
		//Hit each component endpoint
		for(Component comp : componentContainer.getComponents()){
			target = client.target(baseUri+"/"+domainName+"/applications/"+applicationName+"/components/"+comp.getName());
			response = target.request().accept(MediaType.APPLICATION_XML).get();
			assertEquals(200, response.getStatus());

			//Hit properties
			target = client.target(baseUri+"/"+domainName+"/applications/"+applicationName+"/components/"+comp.getName()+"/properties");
			response = target.request().accept(MediaType.APPLICATION_XML).get();
			PropertyContainer propContainer = response.readEntity(PropertyContainer.class);
			assertEquals(200, response.getStatus());

			//Hit each propertyId
			for(Property property : propContainer.getProperties()){
				target = client.target(baseUri+"/"+domainName+"/applications/"+applicationName+"/components/"+comp.getName()+"/properties/"+property.getId());
				response = target.request().accept(MediaType.APPLICATION_XML).get();
				assertEquals(200, response.getStatus());
			}
			
		}
	}
}

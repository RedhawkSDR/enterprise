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

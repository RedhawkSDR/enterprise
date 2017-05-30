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

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.rest.model.Component;
import redhawk.rest.model.ComponentContainer;

public class RedhawkSoftwareComponentResourceIT extends RedhawkResourceTestBase{
	static String applicationName = "MyApplication";

	@BeforeClass
	public static void launchApplication(){
		try {
			driver.getDomain().createApplication(applicationName, "/waveforms/rh/FM_mono_demo/FM_mono_demo.sad.xml");
		} catch (ApplicationCreationException | MultipleResourceException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSoftwareComponents() throws InterruptedException{
		WebTarget target = client.target(baseURI+"/"+domainName+"/applications/"+applicationName+"/components");
		System.out.println(baseURI+"/"+domainName+"/applications/"+applicationName+"/components");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		ComponentContainer componentContainer = response.readEntity(ComponentContainer.class);
		assertEquals(200, response.getStatus());
		
		
		//Hit each component endpoint
		for(Component comp : componentContainer.getComponents()){
			target = client.target(baseURI+"/"+domainName+"/applications/"+applicationName+"/components/"+comp.getName()+"/softwarecomponent");
			response = target.request().accept(MediaType.APPLICATION_XML).get();
			assertEquals(200, response.getStatus());
		}
	}

}

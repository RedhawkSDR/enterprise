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

import java.util.UUID;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Ignore;
import org.junit.Test;

import redhawk.rest.model.WaveformInfo;

public class RedhawkApplicationResourceIT extends RedhawkApplicationResourceTestBase{
	@Test
	public void testGetApplications() throws InterruptedException{
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testGetApplication(){
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName);
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testGetApplicationProperties(){
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/properties");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testCommandAndControlOfWaveform(){
		String applicationName = "restLaunchedApp";
		WebClient client = WebClient.create(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName);
		
		WaveformInfo info = new WaveformInfo();
		info.setSadLocation("/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
		info.setId(UUID.randomUUID().toString());
		info.setName(applicationName);
		
		//Test launching a waveform
		client.type(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML);
		Response r = client.put(info);
		assertEquals(200, r.getStatus());
		
		//Test Stopping a waveform
		client.type(MediaType.APPLICATION_JSON);
		r = client.post("stop");
		assertEquals(200, r.getStatus());
		
		//Test Starting a waveform
		client.type(MediaType.APPLICATION_JSON);
		r = client.post("start");
		assertEquals(200, r.getStatus());
		
		//Test Releasing a waveform 
		r = client.delete();
		assertEquals(200, r.getStatus());
	}
	
	@Test
	@Ignore("Temporarily ignore until you figure out why provider is not working in Integration Tests w/ JSON but works on deployed asset")
	public void testLaunchAndReleaseApplicationJSON(){
		String applicationName = "restLaunchedApp";
		WebClient client = WebClient.create(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName);
		
		WaveformInfo info = new WaveformInfo();
		info.setSadLocation("/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
		info.setId(UUID.randomUUID().toString());
		info.setName(applicationName);
		
		client.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		Response r = client.put(info);
		assertEquals(200, r.getStatus());
		
		r = client.delete();
		assertEquals(200, r.getStatus());
	}
}

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

import redhawk.driver.xml.model.sca.sad.Softwareassembly;
import redhawk.rest.model.WaveformContainer;
import redhawk.rest.model.WaveformInfo;

public class RedhawkWaveformResourceIT extends RedhawkResourceTestBase{
	@Test
	public void testGetWaveforms() throws InterruptedException{
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/waveforms");
		//System.out.println(target.request().get().readEntity(String.class));
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		
		WaveformContainer wvContainer = response.readEntity(WaveformContainer.class);
		assertEquals(200, response.getStatus());		
		
		for(WaveformInfo waveform : wvContainer.getDomains()){
			target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/waveforms/"+waveform.getId());
			response = target.request().accept(MediaType.APPLICATION_XML).get();
			Softwareassembly info = response.readEntity(Softwareassembly.class);
			assertEquals(200, response.getStatus());
		}
	}
}

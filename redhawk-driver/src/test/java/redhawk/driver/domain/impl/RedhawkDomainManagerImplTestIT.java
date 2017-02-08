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
package redhawk.driver.domain.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;


public class RedhawkDomainManagerImplTestIT {
	private RedhawkDomainManager impl;
		
	@Before
	public void setup() throws ResourceNotFoundException, CORBAException{
		RedhawkDriver driver = new RedhawkDriver(); 
		impl = driver.getDomain("REDHAWK_DEV");
	}

	@Test
	public void testRedhawkDomainManagerCreateApplicationWithString() throws ResourceNotFoundException, CORBAException, ApplicationCreationException, MultipleResourceException{
		String waveformFileName = impl.getFileManager().getWaveformFileNames().get(0);
		String applicationName = "MyApplication";
		
		assertEquals("Should be no applications", true, impl.getApplications().isEmpty());
		assertNotNull(null, impl.createApplication(applicationName, waveformFileName));
		assertEquals("Now there should be applications in the domain.", false, impl.getApplications().isEmpty());
		assertEquals("Now there should be 1 application.", 1, impl.getApplications().size());
		assertNotNull("Unable to get application by name: "+applicationName, impl.getApplicationsByName(applicationName));
		String applicationIdentifier = impl.getApplications().get(0).getIdentifier();
		assertNotNull("Unable to get application by identifier "+applicationIdentifier, impl.getApplicationByIdentifier(applicationIdentifier));
	}
	
	@Test
	public void testRedhawkDomainManagerCreateApplicationWithFile() throws ResourceNotFoundException, CORBAException, ApplicationCreationException, MultipleResourceException, ConnectionException, IOException{
		String waveFormFileLocation = "src/test/resources/waveforms/rh/testWaveform.sad.xml";
		String applicationName = "MyApplication";
		
		assertEquals("Should be no applications", true, impl.getApplications().isEmpty());
		assertNotNull(null, impl.createApplication(applicationName, new File(waveFormFileLocation)));
		assertEquals("Now there should be applications in the domain.", false, impl.getApplications().isEmpty());
		assertEquals("Now there should be 1 application.", 1, impl.getApplications().size());
		assertNotNull("Unable to get application by name: "+applicationName, impl.getApplicationsByName(applicationName));
		String applicationIdentifier = impl.getApplications().get(0).getIdentifier();
		assertNotNull("Unable to get application by identifier "+applicationIdentifier, impl.getApplicationByIdentifier(applicationIdentifier));
	
		//Remove the file so this test can be run again 
		impl.getFileManager().removeDirectory("/waveforms/testWaveform");
	}
	
	@Test
	public void testRedhawkDomainManagerGetDeviceManagers() throws MultipleResourceException, ResourceNotFoundException{
		List<RedhawkDeviceManager> deviceManagers = impl.getDeviceManagers();
		
		assertEquals("Unable to retrieve device manager object", false, deviceManagers.isEmpty());
		assertEquals("Unable to retrieve device manager by name", true, impl.getDeviceManagerByName(deviceManagers.get(0).getName())!=null);
		assertEquals("Unable to retrieve device manager by identifier", true, impl.getDeviceManagerByIdentifier(deviceManagers.get(0).getUniqueIdentifier())!=null);		
	}
	
	@Test
	public void testOtherMethods() throws ResourceNotFoundException{
		assertNotNull(impl.getAllocationManager());
		assertNotNull(impl.getConnectionManager());
		assertNotNull(impl.getEventChannelManager());
		assertNotNull(impl.getDomainManagerConfiguration());
		assertNotNull(impl.getPropertyConfiguration());
		assertNotNull(impl.getDomainManagerAssembly());
	}
	
	
	@After
	public void shutdown() throws ApplicationReleaseException{
		for(RedhawkApplication application : impl.getApplications()){
			application.release();
		}
	}
}

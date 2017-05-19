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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.RedhawkUtils;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;
import redhawk.testutils.RedhawkTestBase;


public class RedhawkDomainManagerImplIT extends RedhawkTestBase{
	private RedhawkDomainManager domainManager;
	

	@Before
	public void setup() throws ResourceNotFoundException, CORBAException{
		domainManager = driver.getDomain("REDHAWK_DEV");
	}

	@Test
	public void testRedhawkDomainManagerCreateApplicationWithString() throws ResourceNotFoundException, CORBAException, ApplicationCreationException, MultipleResourceException{
		String waveformFileName = domainManager.getFileManager().getWaveformFileNames().get(0);
		String applicationName = "MyApplication";
		System.out.println("WaveformFileName "+waveformFileName);
		assertEquals("Should be no applications", true, domainManager.getApplications().isEmpty());
		assertNotNull(domainManager.createApplication(applicationName, waveformFileName));
		assertEquals("Now there should be applications in the domain.", false, domainManager.getApplications().isEmpty());
		assertEquals("Now there should be 1 application.", 1, domainManager.getApplications().size());
		assertNotNull("Unable to get application by name: "+applicationName, domainManager.getApplicationsByName(applicationName));
		String applicationIdentifier = domainManager.getApplications().get(0).getIdentifier();
		assertNotNull("Unable to get application by identifier "+applicationIdentifier, domainManager.getApplicationByIdentifier(applicationIdentifier));
	}
	
	@Test
	public void testRedhawkDomainManagerCreateApplicationWithFile() throws ResourceNotFoundException, CORBAException, ApplicationCreationException, MultipleResourceException, ConnectionException, IOException{
		String waveFormFileLocation = "src/test/resources/waveforms/rh/testWaveform.sad.xml";
		String applicationName = "MyApplication";
		
		assertEquals("Should be no applications", true, domainManager.getApplications().isEmpty());
		assertNotNull(null, domainManager.createApplication(applicationName, new File(waveFormFileLocation)));
		assertEquals("Now there should be applications in the domain.", false, domainManager.getApplications().isEmpty());
		assertEquals("Now there should be 1 application.", 1, domainManager.getApplications().size());
		assertNotNull("Unable to get application by name: "+applicationName, domainManager.getApplicationsByName(applicationName));
		String applicationIdentifier = domainManager.getApplications().get(0).getIdentifier();
		assertNotNull("Unable to get application by identifier "+applicationIdentifier, domainManager.getApplicationByIdentifier(applicationIdentifier));
	
		//Remove the file so this test can be run again 
		domainManager.getFileManager().removeDirectory("/waveforms/testWaveform");
	}
	
	
	@Test
	@Ignore("Figure out how to fix this...")
	public void testCreateApplicationWithSAD() throws FileNotFoundException, IOException, ApplicationCreationException{
		Softwareassembly assembly = RedhawkUtils.unMarshalSadFile(new FileInputStream("src/test/resources/waveforms/rh/testWaveform.sad.xml"));
		
		domainManager.createApplication("myApplication", assembly);
	}
	
	@Test
	public void testDeviceInteraction() throws MultipleResourceException{
		List<RedhawkDevice> devices = domainManager.getDevices();
		assertTrue(devices.size() > 0);
		Map<String, RedhawkDevice> deviceMap = domainManager.devices();
		assertTrue(deviceMap.size() > 0);
		
		devices = domainManager.getDevicesByName("GPP.*");
		assertTrue(devices.size() > 0);
		
		RedhawkDevice device = domainManager.getDeviceByName("GPP.*");
		assertTrue(device.getName().startsWith("GPP"));
		
		assertNotNull(domainManager.getDeviceByIdentifier(device.getIdentifier()));
	}
	
	@Test
	public void testRedhawkDomainManagerGetDeviceManagers() throws MultipleResourceException, ResourceNotFoundException{
		List<RedhawkDeviceManager> deviceManagers = domainManager.getDeviceManagers();
		
		assertEquals("Unable to retrieve device manager object", false, deviceManagers.isEmpty());
		assertEquals("Unable to retrieve device manager by name", true, domainManager.getDeviceManagerByName(deviceManagers.get(0).getName())!=null);
		assertEquals("Unable to retrieve device manager by identifier", true, domainManager.getDeviceManagerByIdentifier(deviceManagers.get(0).getUniqueIdentifier())!=null);		
	}
	
	@Test
	public void testOtherMethods() throws ResourceNotFoundException{
		assertNotNull(domainManager.getAllocationManager());
		assertNotNull(domainManager.getConnectionManager());
		assertNotNull(domainManager.getEventChannelManager());
		assertNotNull(domainManager.getDomainManagerConfiguration());
		assertNotNull(domainManager.getPropertyConfiguration());
		assertNotNull(domainManager.getDomainManagerAssembly());
		assertNotNull(domainManager.deviceManagers());		
	}
	
	//Below will be snippets for docs for RedhawkDomainManager
	@Test
	public void testSnippetForCreatingAnApplication() throws MultipleResourceException, ApplicationCreationException, CORBAException, ResourceNotFoundException, ApplicationReleaseException{
		String applicationName = "myApp";
		RedhawkDriver driver = new RedhawkDriver(); 
		RedhawkDomainManager domainManager = driver.getDomain();
		String waveformLocation = "/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml";
		
		//Create an application that already exists in your $SDRROOT
		RedhawkApplication application = domainManager.createApplication(applicationName, waveformLocation);
		
		//Retrieve applications launched in your domain 
		List<RedhawkApplication> applications = domainManager.getApplications();
		
		//Retrieve a specific application in your domain
		application = domainManager.getApplicationByName(applicationName);
		
		//Retrieve all devices in a domain
		List<RedhawkDevice> devices = domainManager.getDevices();
		
		//Retrieve a group of devices by regez 
		devices = domainManager.getDevicesByName("GPP.*");
		
		//Retrieve a device by name 
		String deviceName = devices.get(0).getName();
		RedhawkDevice device = domainManager.getDeviceByName(deviceName);
		
		//Retrieve all available device managers
		List<RedhawkDeviceManager> managers = domainManager.getDeviceManagers();
		
		//Get a specific device manager 
		RedhawkDeviceManager devManager = domainManager.getDeviceManagerByName(managers.get(0).getName()); 
	}
	//End of SNIPPETS for RedhawkDomainManager
	
	@After
	public void shutdown() throws ApplicationReleaseException{
		for(RedhawkApplication application : domainManager.getApplications()){
			application.release();
		}
		driver.disconnect();
	}
}

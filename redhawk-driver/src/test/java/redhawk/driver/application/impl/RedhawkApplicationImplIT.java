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
package redhawk.driver.application.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.ApplicationStartException;
import redhawk.driver.exceptions.ApplicationStopException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.port.RedhawkPortStatistics;
import redhawk.driver.port.impl.RedhawkExternalPortImpl;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkApplicationImplIT extends RedhawkTestBase {
	private static String applicationName = "myTestApplication";

	private static RedhawkApplication application;

	@BeforeClass
	public static void setup()
			throws ResourceNotFoundException, ApplicationCreationException, CORBAException, MultipleResourceException {
		driver.getDomain("REDHAWK_DEV").createApplication(applicationName,
				new File("src/test/resources/waveforms/rh/testWaveform.sad.xml"));

		application = driver.getApplication("REDHAWK_DEV/" + applicationName);

		assertNotNull(application);
	}

	@Test
	public void testApplicationLifeCycleManagement() throws ApplicationStartException, ApplicationStopException {
		assertEquals(applicationName, application.getName());
		application.start();
		assertEquals("Application should be started", true, application.isStarted());
		application.stop();
		assertEquals("Application should be stopped", false, application.isStarted());
	}

	@Test
	public void testGetAssembly() throws IOException {
		assertNotNull(application.getAssembly());
	}

	@Test
	public void testGetComponents() throws MultipleResourceException, ResourceNotFoundException {
		List<RedhawkComponent> redhawkComponents = application.getComponents();
		assertEquals("There should be two components in the test waveform", 2, redhawkComponents.size());

		// Make sure you can retrieve each component by name
		for (RedhawkComponent component : redhawkComponents) {
			assertNotNull(application.getComponentByName(component.getName()));
		}
	}

	// @Test TODO: Fix below logic
	public void snippets() throws ApplicationStopException, ApplicationStartException, MultipleResourceException,
			ResourceNotFoundException, ApplicationReleaseException, ApplicationCreationException, CORBAException {
		// Get all components
		List<RedhawkComponent> components = application.getComponents();

		// Get a specific component
		String componentName = components.get(0).getName();
		RedhawkComponent component = application.getComponentByName(componentName);

		// Example code for managing an applications lifecyle
		// Stop an application
		application.stop();

		// Start an application
		application.start();

		// Check to see if an application is started
		if (application.isStarted())
			application.stop();

		// Release an application
		application.release();

		// Above release is just for show other test in here may need that app
		// so relaunch cause order of tests running is
		// not gauranteed.
		driver.getDomain("REDHAWK_DEV").createApplication(applicationName,
				"/waveforms/testWaveform/testWaveform.sad.xml");
	}

	// Test retrieving external ports
	@Test
	public void testGetExternalPortsAndStats() throws ResourceNotFoundException, ApplicationCreationException, CORBAException,
			MultipleResourceException, IOException {
		RedhawkApplication extApplication = null; 
		try {
			// Launch application with External ports
			String appName = "externalPortsApp";

			extApplication = driver.getDomain("REDHAWK_DEV").createApplication(appName,
					new File("src/test/resources/waveforms/ExternalPropPortExample/ExternalPropPortExample.sad.xml"));

			// Should be two external ports
			assertEquals("Should be two external ports in this waveform", 4, extApplication.getPorts().size());
			logger.info(application.getPorts().toString());
			// Ensure you properly get properties related to external ports
			RedhawkExternalPortImpl externalPort = (RedhawkExternalPortImpl) extApplication.getPort("sigGenPort");

			assertNotNull(externalPort);
			assertNotNull(externalPort.getDescription());
			
			/*
			 * Test retrieving each ports stats
			 */
			for(RedhawkPort port : extApplication.getPorts()){
				assertNotNull(port.getPortStatistics());
			}
			
			/*
			 * Test retrieving a ports stats
			 */
			List<RedhawkPortStatistics> stats = extApplication.getPort("hardLimitPort").getPortStatistics();
			System.out.println(stats);
		} finally {
			if (extApplication != null) {
				try {
					extApplication.release();

					driver.getDomain().getFileManager().removeDirectory("/waveforms/ExternalPropPortExample");
				} catch (ApplicationReleaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	// Test retrieving External Properties
	@Test
	public void testGetExternalProperties() throws ResourceNotFoundException, ApplicationCreationException, CORBAException, MultipleResourceException, IOException{
		RedhawkApplication extApplication = null; 
		try {
			// Launch application with External ports
			String appName = "externalPropertiesApp";

			extApplication = driver.getDomain("REDHAWK_DEV").createApplication(appName,
					new File("src/test/resources/waveforms/ExternalPropPortExample/ExternalPropPortExample.sad.xml"));

			assertEquals("Should be 12 properties w/ External and AssemblyController props", 12, extApplication.getProperties().size());			
			assertEquals("Should be 3 external properties", 3, extApplication.getExternalProperties().size());
			assertNotNull(extApplication.getProperty("siggen_freq", "siggen2_freq"));
		} finally {
			if (extApplication != null) {
				try {
					extApplication.release();

					driver.getDomain().getFileManager().removeDirectory("/waveforms/ExternalPropPortExample");
				} catch (ApplicationReleaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	}
	
	@Test
	public void testAware(){
		try{
			//Just test whether you can successfully call the method
			Boolean isAware = application.isAware();
			logger.info("Aware "+isAware);
		}catch(Exception ex){
			fail("Unable to call aware method"+ex.getMessage());
		}
	}
	
	@Test
	public void testComponentDevices(){
		Map<String, RedhawkDevice> compToDeviceMap = application.getComponentDevices();
		
		//Map has stuff in it
		assertTrue(!compToDeviceMap.isEmpty());
		assertEquals("Should be 2 entries", 2, compToDeviceMap.size());
	}
	
	@Test
	public void testComponentProcessIds(){
		Map<String, Integer> compToProcess = application.getComponentProcessIds();
		
		//Map has stuff in it
		assertTrue(!compToProcess.isEmpty());
		assertEquals("Should be 2 entries", 2, compToProcess.size());
	}
	
	@Test
	public void testComponentImplementation() {
		Map<String, String> compImpl = application.getComponentImplementations();
		
		//Map has stuff in it
		assertTrue(!compImpl.isEmpty());
		assertEquals("Should be 2 entries", 2, compImpl.size());
		
		//All values should be cpp
		for(String impl : compImpl.values()) {
			assertEquals("cpp", impl);
		}
	}
	
	@Test
	public void testWaveformMetrics() throws InterruptedException {
		String[] components = new String[0];
		String[] attributes = new String[0];
		HashSet<String> expectedComponentKeys = new HashSet<String>(Arrays.asList("valid", "shared", "processes", "cores", "memory", "threads", "files", "componenthost"));
		HashSet<String> expectedApplicationKeys = new HashSet<String>(Arrays.asList("valid", "processes", "cores", "memory", "threads", "files"));

		//Expecting three keys 
		try {
			application.start();
			
			//Needs time to start up
			Thread.sleep(1000l);
			
			Map<String, Map<String, Object>> metrics = application.getMetrics();
			
			//Ensure expeceted number of entries in map
			assertEquals("Should be 3 entries in map", 3, metrics.keySet().size());
			
			//
			for(Map.Entry<String, Map<String, Object>> entry : metrics.entrySet()) {
				if(entry.getValue().size()==expectedComponentKeys.size()) {
					assertEquals(expectedComponentKeys, entry.getValue().keySet());
				}else {
					assertEquals(expectedApplicationKeys, entry.getValue().keySet());
				}
				
				//Make sure no null values 
				this.noNullValues(entry.getValue());
			}
		} catch (ApplicationStartException | ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testWaveformMetricsWithFilters() throws InterruptedException {
		String[] components = new String[0];
		String[] attributes = new String[]{"valid", "shared", "processes", "cores", "memory", "files", "componenthost"};
		
		//Removed threads from expected list
		HashSet<String> expectedComponentKeys = new HashSet<String>(Arrays.asList("valid", "shared", "processes", "cores", "memory", "files", "componenthost"));
		HashSet<String> expectedApplicationKeys = new HashSet<String>(Arrays.asList("valid", "processes", "cores", "memory", "files"));
		
		try {
			application.start();
			
			//Needs time to start up
			Thread.sleep(1000l);
			
			Map<String, Map<String, Object>> metrics = application.getMetrics(components, attributes);
			
			//Ensure expeceted number of entries in map
			assertEquals("Should be 3 entries in map", 3, metrics.keySet().size());
			
			/*
			 * Filter by attribute
			 */
			for(Map.Entry<String, Map<String, Object>> entry : metrics.entrySet()) {
				if(entry.getValue().size()==expectedComponentKeys.size()) {
					assertEquals(expectedComponentKeys, entry.getValue().keySet());
				}else {
					assertEquals(expectedApplicationKeys, entry.getValue().keySet());
				}
				
				//Make sure no null values 
				this.noNullValues(entry.getValue());
			}
			
			//Filter by component
			//TODO: Shouldn't need to split should minimum be a helper method here.
			String[] compFilter = new String[] {application.getComponents().get(0).getName().split(":")[0]};
			
			metrics = application.getMetrics(compFilter, attributes);

			assertEquals("Should only be 1 entry", 1, metrics.size());
			
			//Attributes should match filter
			assertEquals("Make sure keys are correct", expectedComponentKeys, metrics.get(compFilter[0].toString()).keySet());
		} catch (ApplicationStartException | ApplicationException e) {
			fail("Exception thrown during test"+e.getMessage());
		}
	}
	
	private void noNullValues(Map<String, Object> map) {
		for(Object key : map.values()) {
			assertNotNull("No values in the map should be null", key);
		}
	}
}

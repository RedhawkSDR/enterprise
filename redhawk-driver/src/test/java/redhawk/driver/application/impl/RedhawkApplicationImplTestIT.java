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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import redhawk.RedhawkTestBase;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.ApplicationStartException;
import redhawk.driver.exceptions.ApplicationStopException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkApplicationImplTestIT extends RedhawkTestBase{	
	private static String applicationName = "myTestApplication"; 
	
	private static RedhawkApplication application; 
	
	@BeforeClass
	public static void setup() throws ResourceNotFoundException, ApplicationCreationException, CORBAException, MultipleResourceException{
		driver.getDomain("REDHAWK_DEV").createApplication(applicationName, new File("src/test/resources/waveforms/rh/testWaveform.sad.xml"));
		
		application = driver.getApplication("REDHAWK_DEV/"+applicationName);

		assertNotNull(application);
	}
	
	
	@Test
	public void testApplicationLifeCycleManagement() throws ApplicationStartException, ApplicationStopException{
		assertEquals(applicationName, application.getName());
		application.start();
		assertEquals("Application should be started", true, application.isStarted());
		application.stop();
		assertEquals("Application should be stopped", false, application.isStarted());
	}
	
	@Test
	public void testGetAssembly() throws IOException{
		assertNotNull(application.getAssembly());	
	}
	
	@Test
	public void testGetComponents() throws MultipleResourceException, ResourceNotFoundException{
		List<RedhawkComponent> redhawkComponents = application.getComponents();
		assertEquals("There should be two components in the test waveform", 2, redhawkComponents.size());
		
		//Make sure you can retrieve each component by name 
		for(RedhawkComponent component : redhawkComponents){
			assertNotNull(application.getComponentByName(component.getName()));
		}	
	}
	
	//@Test TODO: Fix below logic
	public void snippets() throws ApplicationStopException, ApplicationStartException, MultipleResourceException, ResourceNotFoundException, ApplicationReleaseException, ApplicationCreationException, CORBAException{
		//Get all components
		List<RedhawkComponent> components = application.getComponents();
		
		//Get a specific component
		String componentName = components.get(0).getName();
		RedhawkComponent component = application.getComponentByName(componentName);
		
		//Example code for managing an applications lifecyle
		//Stop an application 
		application.stop();
		
		//Start an application
		application.start();
		
		//Check to see if an application is started
		if(application.isStarted())
			application.stop();
		
		//Release an application
		application.release();
	
		//Above release is just for show other test in here may need that app so relaunch cause order of tests running is 
		//not gauranteed.
		driver.getDomain("REDHAWK_DEV").createApplication(applicationName, "/waveforms/testWaveform/testWaveform.sad.xml");
	}
	
	//TODO: Add test for getting externalports
	
	@AfterClass
	public static void shutdown() throws ApplicationReleaseException, ConnectionException, ResourceNotFoundException, IOException, CORBAException{
		//Release application and clean it up from $SDRROOT
		if(application!=null){
			application.release();
		}
		driver.getDomain("REDHAWK_DEV").getFileManager().removeDirectory("/waveforms/testWaveform");			
	}
}

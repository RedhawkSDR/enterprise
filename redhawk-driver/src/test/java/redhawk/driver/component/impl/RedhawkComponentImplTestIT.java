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
package redhawk.driver.component.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redhawk.RedhawkTestBase;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ComponentStartException;
import redhawk.driver.exceptions.ComponentStopException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.properties.RedhawkProperty;
import redhawk.driver.properties.RedhawkSimple;

public class RedhawkComponentImplTestIT extends RedhawkTestBase{
		private String applicationName = "myTestApplication"; 
	
	private RedhawkApplication application; 
	
	private List<RedhawkComponent> components; 
	
	@Before
	public void setup() throws ResourceNotFoundException, ApplicationCreationException, CORBAException, MultipleResourceException{
		driver.getDomain("REDHAWK_DEV").createApplication(applicationName, new File("src/test/resources/waveforms/rh/testWaveform.sad.xml"));
		application = driver.getApplication("REDHAWK_DEV/"+applicationName);
		assertNotNull(application);
		components = application.getComponents();
	}
	
	@Test
	public void testComponentManagementLifecycle() throws ComponentStartException, ComponentStopException{
		for(RedhawkComponent component : components){
			component.start();
			assertEquals("Component should be started", true, component.started());
			component.stop();
			assertEquals("Component should be stopped.", false, component.started());
		}
	}
	
	@Test
	public void testAccessToComponentPorts() throws ResourceNotFoundException, MultipleResourceException{
		for(RedhawkComponent component : components){
			for(RedhawkPort port : component.getPorts()){
				assertNotNull(component.getPort(port.getName()));
			}
		}
	}
	
	//SNIPPET 
	@Test
	public void snippets() throws Exception{
		//Get your component
		RedhawkComponent component = application.getComponentByName("SigGen.*");
		
		//Retrieve properties that are avaiable
		Map<String, RedhawkProperty> propertiesMap = component.getProperties();
		
		//Change a specific property 
		String propertyName = "sample_rate";
		RedhawkSimple simpleProp = (RedhawkSimple) propertiesMap.get(propertyName);
		simpleProp.setValue(1000);
		
		//Stop a component
		component.stop();
		
		//Start a component
		component.start();
		
		//Check if a component is started 
		if(!component.started())
			component.start();
	}
	//SNIPPET
	
	@Test
	public void testAccessToComponentProperties() throws ResourceNotFoundException, MultipleResourceException{
		for(RedhawkComponent component : components){
			for(String propertyName : component.getProperties().keySet()){
				assertNotNull(component.getProperty(propertyName));
			}
		}
	}
	
	@After
	public void shutdown() throws ApplicationReleaseException, ConnectionException, ResourceNotFoundException, IOException, CORBAException{
		//Release application and clean it up from $SDRROOT
		application.release();
		driver.getDomain("REDHAWK_DEV").getFileManager().removeDirectory("/waveforms/testWaveform");
	}
}

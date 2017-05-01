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
package redhawk.driver.properties;

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
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkPropertyIT extends RedhawkTestBase{
	private String applicationName = "myTestApplication"; 
	
	private RedhawkApplication application; 
	
	private List<RedhawkComponent> components; 
	
	private List<RedhawkProperty> properties; 
	
	@Before
	public void setup() throws ResourceNotFoundException, ApplicationCreationException, CORBAException, MultipleResourceException{
		driver.getDomain("REDHAWK_DEV").createApplication(applicationName, new File("src/test/resources/waveforms/rh/testWaveform.sad.xml"));
		application = driver.getApplication("REDHAWK_DEV/"+applicationName);
		assertNotNull(application);
		components = application.getComponents();
	}
	
	@Test
	public void testSettingProperties() throws Exception{
		for(RedhawkComponent component : components){
			for(Map.Entry<String, RedhawkProperty> propertyEntry : component.getProperties().entrySet()){
				RedhawkProperty property = propertyEntry.getValue();
				if(property instanceof RedhawkSimple){
					RedhawkSimple simpleProperty = (RedhawkSimple) property;
					if(simpleProperty.getValue() instanceof String){
						simpleProperty.setValue("Foo");
						assertEquals("Foo", simpleProperty.getValue());
					}else if(simpleProperty.getValue() instanceof Double){
						simpleProperty.setValue(10d);
						assertEquals(10d, simpleProperty.getValue());
					}else if(simpleProperty.getValue() instanceof Boolean){
						Boolean originalVal = (Boolean)simpleProperty.getValue();
						simpleProperty.setValue(!originalVal);
						assertEquals(!originalVal, simpleProperty.getValue());
					}else if(simpleProperty.getValue() instanceof Integer){
						simpleProperty.setValue(7);
						assertEquals(7, simpleProperty.getValue());
					}else if(simpleProperty.getValue() instanceof Float){
						simpleProperty.setValue(700f);
						assertEquals(700f, simpleProperty.getValue());
					}else{
						System.out.println("Property type not accounted for "+simpleProperty.getValue().getClass());
					}
				}else{
					System.out.println(property.getClass());
				}
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

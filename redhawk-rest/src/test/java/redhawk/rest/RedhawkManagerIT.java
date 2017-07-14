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
package redhawk.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.rest.model.Application;
import redhawk.rest.model.ExternalPort;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.Port;
import redhawk.rest.model.PropertyContainer;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkManagerIT extends RedhawkTestBase{
	private static RedhawkManager manager = new RedhawkManager(); 
	
	private static RedhawkApplication externalApplication, basicApplication;
	
	private static String applicationName = "ExternalPropsApp";
	
	private static String noExternalPropsPortsApp = "basicApp";

	@BeforeClass
	public static void setup(){
		//Launch app 
		try {
			externalApplication = driver.getDomain().createApplication(applicationName, 
					new File("../redhawk-driver/src/test/resources/waveforms/ExternalPropPortExample/ExternalPropPortExample.sad.xml"));
			basicApplication = driver.getDomain().createApplication(noExternalPropsPortsApp, 
					new File("../redhawk-driver/src/test/resources/waveforms/rh/testWaveform.sad.xml"));
		} catch (MultipleResourceException | ApplicationCreationException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testApplicationsWithExternalPortsAndProperties(){
		try {
			List<Application> applications = manager.getAll(domainHost+":2809", "application", "REDHAWK_DEV", FetchMode.EAGER);
		
			assertEquals("Should be atleast 2 app", 2, applications.size());
			
			for(Application application : applications){
				if(application.getName().equals(applicationName)){
					this.externalApplicationAsserts(application);			
				}else{
					//Should be no external ports  
					assertEquals("No external properties", true, application.getExternalPorts().isEmpty());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testApplicationWithExternalPortsAndProperties(){
		try {
			Application application = manager.get(domainHost+":2809", "application", "REDHAWK_DEV/External.*");
		
			assertNotNull("Should be 1 app", application);
			this.externalApplicationAsserts(application);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetApplicationWithNoExternalProperties(){
		try {
			PropertyContainer container = manager.getProperties(domainHost+":2809", "application", "REDHAWK_DEV/"+noExternalPropsPortsApp);
			assertNotNull(container);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			fail("FAILED!!!!"+e.getMessage());
		}
	}
	
	private void externalApplicationAsserts(Application application){
		assertEquals("Properties should be here", true, !application.getProperties().isEmpty());
		assertEquals("Should be three external ports", 3, application.getExternalPorts().size());
	
		//Make sure external ports contain the externalname
		List<ExternalPort> ports = application.getExternalPorts();
		for(ExternalPort port : ports){
			assertNotNull(port.getExternalname());
		}		
	}
	
	@AfterClass
	public static void cleanupManager(){
		if(externalApplication!=null){
			try {
				externalApplication.release();
				basicApplication.release();
				
				driver.getDomain().getFileManager().removeDirectory("/waveforms/ExternalPropPortExample");
				driver.getDomain().getFileManager().removeDirectory("/waveforms/testWaveform");			
			} catch (ApplicationReleaseException | ConnectionException | MultipleResourceException | IOException | CORBAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
}

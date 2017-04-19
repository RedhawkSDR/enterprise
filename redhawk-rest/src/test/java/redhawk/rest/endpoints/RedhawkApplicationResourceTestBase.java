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

import org.junit.AfterClass;
import org.junit.BeforeClass;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkApplicationResourceTestBase extends RedhawkResourceTestBase{
	static RedhawkDriver driver;
	
	static RedhawkDomainManager domain; 
	
	static String applicationName = "MyApplication";
	
	@BeforeClass
	public static void setupApplication() throws ResourceNotFoundException, ApplicationCreationException, CORBAException{
		driver = new RedhawkDriver();
		domain = driver.getDomain("REDHAWK_DEV");
		
		domain.createApplication(applicationName, "/waveforms/rh/FM_mono_demo/FM_mono_demo.sad.xml");
	}
	
	@AfterClass
	public static void tearDownApplication() throws ApplicationReleaseException{
		for(RedhawkApplication application : domain.getApplications()){
			application.release();
		}
	}
}

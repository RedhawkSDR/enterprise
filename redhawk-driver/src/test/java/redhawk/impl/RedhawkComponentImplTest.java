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
package redhawk.impl;

import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.xml.model.sca.scd.Provides;
import redhawk.driver.xml.model.sca.scd.Uses;

@Ignore("Using this test to make sure bidirectional ports are displayed appropriately")
public class RedhawkComponentImplTest {
	private Redhawk redhawk;
	
	private String hostName = "localhost"; 
	
	private Integer port = 2809; 

	private String domainName = "REDHAWK_DEV";

	private String applicationId = "TestBiWaveForm";
	
	private String componentId = "BiHelloWorld_1:TestBiWaveForm_1";
	
	private String sinkApplicationId = "mySinkWaveForm";
	
	private String sinkComponentId = "sinksocket_1:mySinkWaveForm_1";

	@Before
	public void setup() throws Exception{
		/*
		 * Initialize Driver
		 */
		redhawk = new RedhawkDriver(hostName, port);
		
	}
	
	@Test
	public void test() throws Exception{ 
		for(Object port : redhawk.getDomain(domainName).getApplicationByName(applicationId).getComponentByName(componentId).getSoftwareComponent().getComponentfeatures().getPorts().getProvidesAndUses()){
			if(port instanceof Uses){
				System.out.println("Uses "+((Uses)port).getUsesname()); 
			}else if(port instanceof Provides){
				System.out.println("Provides "+((Provides)port).getProvidesname()); 				
			}
		}
	}
	
	@Test
	public void testPortPerformance() throws MultipleResourceException, Exception{
		RedhawkComponent rhApplication = redhawk.getDomain(domainName).getApplicationByName(sinkApplicationId).getComponentByName(sinkComponentId); 
		
		System.out.println(new Date());
		rhApplication.getPorts(); 
		System.out.println(new Date());
	}
}

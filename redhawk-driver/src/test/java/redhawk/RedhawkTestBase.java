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
package redhawk;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.eventchannel.RedhawkEventChannel;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;

public class RedhawkTestBase {

	private static MockRedhawkDomain redhawkDomain;

	protected Redhawk redhawk;
	
	protected RedhawkDomainManager rhDomainManager;
	
	RedhawkApplication rhApplication; 
	
	RedhawkPort rhPort;
	
	RedhawkComponent rhComponent; 
	
	RedhawkDeviceManager rhDeviceManager;
	
	RedhawkDevice rhDevice;
	
	@BeforeClass
	public static void startRedhawk() {
		redhawkDomain = new MockRedhawkDomain();
		redhawkDomain.start();
	}
	
	@Before
	public void setup() throws MultipleResourceException, ResourceNotFoundException, IOException, ConnectionException, CORBAException{
		
		//redhawk = new RedhawkDriver("127.0.0.1", 2809);
		redhawk = Mockito.mock(RedhawkDriver.class);
		rhDomainManager = Mockito.mock(RedhawkDomainManager.class);
		rhApplication = Mockito.mock(RedhawkApplication.class);
		rhPort = Mockito.mock(RedhawkPort.class); 
		rhComponent = Mockito.mock(RedhawkComponent.class);
		rhDeviceManager = Mockito.mock(RedhawkDeviceManager.class);
		rhDevice = Mockito.mock(RedhawkDevice.class); 
		List<RedhawkEventChannel> eventChannels = Mockito.mock(List.class);
		
		
		Mockito.when(redhawk.getDomain("aDomain")).thenReturn(rhDomainManager);
		//TODO: No such method exists...
		//Mockito.when(redhawk.getAlias()).thenReturn("anAlias");
		Mockito.when(rhDomainManager.getApplicationByName("applicationName")).thenReturn(rhApplication);
		Mockito.when(rhApplication.getExternalPort("aExternalPort")).thenReturn(rhPort);
		Mockito.when(rhApplication.getComponentByName("componentName")).thenReturn(rhComponent);
		Mockito.when(rhComponent.getPort("aPort")).thenReturn(rhPort);
		//TODO: Figure out a way to mock this...
		//Mockito.when(rhDomainManager.getEventChannelManager().getEventChannels()).thenReturn(eventChannels);
		Mockito.when(rhDomainManager.getDeviceManagerByName("aDeviceManagerName")).thenReturn(rhDeviceManager);
		Mockito.when(rhDeviceManager.getDeviceByName("aDeviceName")).thenReturn(rhDevice);
		Mockito.when(rhDevice.getPort("aDevicePort")).thenReturn(rhPort);
		
	}
	
	
	
	@After
	public void tearDown() {
		redhawk.disconnect();
	}
	
	@AfterClass
	public static void stopRedhawk() {
		redhawkDomain.shutdown();
	}
	
}

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
package redhawk.driver.eventchannel.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import redhawk.RedhawkTestBase;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.eventchannel.RedhawkEventChannel;
import redhawk.driver.eventchannel.RedhawkEventChannelManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.EventChannelCreationException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkEventChannelManagerTestIT extends RedhawkTestBase{
	private RedhawkDomainManager impl;
	
	private RedhawkEventChannelManager eventChannelManager; 
	
	@Before
	public void setup() throws ResourceNotFoundException, CORBAException{
		impl = driver.getDomain("REDHAWK_DEV");
		eventChannelManager = impl.getEventChannelManager(); 
	}
	
	@Test
	public void testInspectEventChannels() throws MultipleResourceException, ResourceNotFoundException{
		List<RedhawkEventChannel> eventChannels = eventChannelManager.getEventChannels();
		
		for(RedhawkEventChannel eventChannel : eventChannels){
			assertNotNull(eventChannelManager.getEventChannel(eventChannel.getName()));
		}
	}
	
	@Test
	public void testEventChannelManagement() throws EventChannelCreationException, MultipleResourceException, ResourceNotFoundException{
		String eventChannelName = "MyEventChannel";
		Integer initialChannelCount = eventChannelManager.getEventChannels().size();
		eventChannelManager.createEventChannel(eventChannelName);
		assertNotNull(eventChannelManager.getEventChannel(eventChannelName));
		eventChannelManager.releaseEventChannel(eventChannelName);
		assertEquals("Should only be "+initialChannelCount+" channels since I removed mine", initialChannelCount, new Integer(eventChannelManager.getEventChannels().size()));
	}
	
	@Test
	public void snippets() throws MultipleResourceException, CORBAException, EventChannelCreationException, ResourceNotFoundException{
		RedhawkDriver driver = new RedhawkDriver(); 
		RedhawkEventChannelManager ecManager = driver.getDomain().getEventChannelManager();
		
		//Get the event channels in your domain
		List<RedhawkEventChannel> eventChannels = ecManager.getEventChannels();
		
		//Create an Event Channel 
		String eventChannelName  = "myEventChannel";
		ecManager.createEventChannel(eventChannelName);
		
		//Retrieve an Event Channel 
		RedhawkEventChannel ec = ecManager.getEventChannel(eventChannelName);
		
		//Release an Event Channel 
		ecManager.releaseEventChannel(eventChannelName);
	}
}

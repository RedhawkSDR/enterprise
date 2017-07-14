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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import redhawk.driver.eventchannel.RedhawkEventChannelManager;
import redhawk.driver.eventchannel.listeners.GenericEventListener;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.EventChannelCreationException;
import redhawk.driver.exceptions.EventChannelException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.rest.model.EventChannel;
import redhawk.rest.model.FetchMode;
import redhawk.testutils.RedhawkTestBase;

/*
 * Adding tests for dynamically getting Event Channel Manager
 */
public class EventChannelManagerIT extends RedhawkTestBase{
	private static RedhawkManager manager = new RedhawkManager(); 
	
	@Test
	public void testGetEventChannels(){
		try {
			List<EventChannel> eventChannels = manager.getAll(domainHost+":2809", "eventchannel", "REDHAWK_DEV", FetchMode.EAGER);
			System.out.println("============================================");
			System.out.println("Are there any event channels");
			System.out.println(driver.getDomain().getEventChannelManager().getEventChannels());
			System.out.println("============================================");
			assertNotNull(eventChannels);
			assertEquals("Should be atleast the 2 default event channels", true, eventChannels.size()>0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetEventChannel(){
		try {
			EventChannel ec = manager.get(domainHost+":2809", "eventchannel", "REDHAWK_DEV", "IDM_Channel");
		
			//Something should come back
			assertNotNull(ec);
			assertEquals("Name should be IDM_Channel", "IDM_Channel", ec.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUnsubscribe(){
		String subscriptionId = "mySub";
		
		try {
			Integer originalSize = driver.getDomain().getEventChannelManager().getEventChannel("ODM_Channel").getRegistrants(1000).size();
			driver.getDomain().getEventChannelManager().getEventChannel("ODM_Channel").subscribe(new GenericEventListener(){
				@Override
				public void onMessage(Object message) {
					System.out.println("Hello World");
				}
				
			}, subscriptionId);
			Integer expectedSize = originalSize+1;
			assertEquals("Number of channels should be "+expectedSize, expectedSize, 
					new Integer(driver.getDomain().getEventChannelManager().getEventChannel("ODM_Channel").getRegistrants(1000).size()));
			
			manager.unsubscribeFromEventChannel(domainHost+":2809", "REDHAWK_DEV", "ODM_Channel", subscriptionId);
			
			assertEquals("Number should now be back to original size "+originalSize, originalSize, 
					new Integer(driver.getDomain().getEventChannelManager().getEventChannel("ODM_Channel").getRegistrants(1000).size()));
		} catch (MultipleResourceException | ResourceNotFoundException | EventChannelException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void createAndShutownofEventChannel(){
		String eventChannelName = "Foo";
		
		try {
			//Create an event channel 
			manager.createEventChannel(domainHost+":2809", "REDHAWK_DEV", eventChannelName);
			
			//Make sure it was created
			assertNotNull(manager.get(domainHost+":2809", "eventchannel", "REDHAWK_DEV", eventChannelName));
		
			//Delete an event channel
			manager.deleteEventChannel(domainHost+":2809", "REDHAWK_DEV", eventChannelName);
			
			try{
				manager.get(domainHost+":2809", "eventchannel", "REDHAWK_DEV", eventChannelName);
				fail("Should have thrown an exception indication Event Channel does not exist");
			}catch(Exception ex){
				//Exception should be thrown because event channel does not exist.
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

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
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Map;

import org.junit.Test;
import org.omg.CosEventChannelAdmin.EventChannel;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.eventchannel.listeners.GenericEventListener;
import redhawk.driver.eventchannel.listeners.MessageListener;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.ApplicationStartException;
import redhawk.driver.exceptions.ApplicationStopException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.EventChannelException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkEventChannelIT extends RedhawkTestBase{
	@Test
	public void testGetRegistrants() throws MultipleResourceException, ResourceNotFoundException, CORBAException{
		RedhawkEventChannelImpl impl = (RedhawkEventChannelImpl) driver.getDomain().getEventChannelManager().getEventChannel("IDM_Channel");
		
		//Should be 2 registrants
		assertEquals("Should be 2 registrants from the GPP", 2, impl.getRegistrants(1000).size());
	}
	
	@Test
	public void testGetCorbaObj() throws MultipleResourceException, ResourceNotFoundException, EventChannelException, CORBAException{
		EventChannel channel = driver.getDomain().getEventChannelManager().getEventChannel("IDM_Channel").getCorbaObj();
		assertNotNull(channel);
	}
	
	@Test
	public void testUnregister(){
		try {
			String subscriptionId = "listenHere";
			
			RedhawkEventChannelImpl impl = (RedhawkEventChannelImpl) driver.getDomain().getEventChannelManager().getEventChannel("IDM_Channel");
		
			//Register a Listener
			impl.subscribe(new MessageListener(){
				@Override
				public void onMessage(Map<String, Object> message) {
					// TODO Auto-generated method stub
					System.out.println("Hello World");
				}
			}, subscriptionId);
			
			//Should now be three registrants
			assertEquals("Should now be 3 registrants", 3, impl.getRegistrants(1000).size());
			
			//Unregister a registrant you added
			//TODO: Add a helper method at this level user only needs to know Id
			impl.unsubscribe(new RedhawkEventRegistrant(subscriptionId, impl.getName(), null));
			
			assertEquals("Should now be 2 registrants", 2, impl.getRegistrants(1000).size());
		} catch (MultipleResourceException | ResourceNotFoundException | CORBAException | EventChannelException e) {
			fail("No exceptions should've been thrown"+e.getMessage());
		}
	}
	
	//Test listening to messages on ODM_Channel
	@Test
	public void testSubscribe(){
		String subscriptionId = "odmListener";
		RedhawkApplication app = null;
		try {
			RedhawkEventChannelImpl impl = (RedhawkEventChannelImpl) driver.getDomain().getEventChannelManager().getEventChannel("ODM_Channel");
			
			MyMessageListener testMessageListener = new MyMessageListener(); 
			//Register a listener and do stuff on events 
			impl.subscribe(testMessageListener);
			
			//Launch an applicatication to generate an ODM_Message
			app = driver.getDomain("REDHAWK_DEV").createApplication("odmTest",
					new File("src/test/resources/waveforms/rh/testWaveform.sad.xml"));
			
			//Stop/Start an application to generate an ODM_Message
			app.stop();
			app.start();
			app.release();
			impl.unsubscribe();
			
			//TODO: Should be exactly 4
			assertEquals("Should be four events based on above actions on application", true, testMessageListener.getMessageCount()>=3);
			app=null;
		} catch (MultipleResourceException | ResourceNotFoundException | CORBAException | EventChannelException | ApplicationCreationException | ApplicationStartException | ApplicationStopException | ApplicationReleaseException e) {
			e.printStackTrace();
			fail("Issue during test "+e.getMessage());
		} finally{
			if(app!=null){
				try {
					app.release();
				} catch (ApplicationReleaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}

	}
	
	class MyMessageListener extends GenericEventListener{
		private Integer messageCount = 0; 
		
		public Integer getMessageCount() {
			return messageCount;
		}

		@Override
		public void onMessage(Object message) {
			messageCount++;
			System.out.println("Received a message "+message);
			System.out.println("Message count: "+messageCount);
		}
	}
}

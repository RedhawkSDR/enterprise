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

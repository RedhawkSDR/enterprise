package redhawk.driver.eventchannel.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

import redhawk.driver.eventchannel.listeners.MessageListener;
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
}

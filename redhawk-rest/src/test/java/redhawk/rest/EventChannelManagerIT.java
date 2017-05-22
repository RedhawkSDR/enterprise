package redhawk.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import redhawk.driver.eventchannel.RedhawkEventChannelManager;
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
			List<EventChannel> eventChannels = manager.getAll("localhost:2809", "eventchannel", "REDHAWK_DEV", FetchMode.EAGER);
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
			EventChannel ec = manager.get("localhost:2809", "eventchannel", "REDHAWK_DEV", "IDM_Channel");
		
			//Something should come back
			assertNotNull(ec);
			assertEquals("Name should be IDM_Channel", "IDM_Channel", ec.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package redhawk.driver.eventchannel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.EventChannelCreationException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkEventChannelManagerTestIT {
	private RedhawkDomainManager impl;
	
	private RedhawkEventChannelManager eventChannelManager; 
	
	@Before
	public void setup() throws ResourceNotFoundException, CORBAException{
		RedhawkDriver driver = new RedhawkDriver(); 
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
		eventChannelManager.createEventChannel(eventChannelName);
		assertNotNull(eventChannelManager.getEventChannel(eventChannelName));
		eventChannelManager.releaseEventChannel(eventChannelName);
		assertEquals("Should only be 2 channels since I removed mine", 2, eventChannelManager.getEventChannels().size());
	}
}

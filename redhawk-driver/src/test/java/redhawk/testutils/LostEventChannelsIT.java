package redhawk.testutils;

import org.junit.Test;

import redhawk.driver.eventchannel.RedhawkEventChannel;
import redhawk.driver.eventchannel.RedhawkEventChannelManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.EventChannelCreationException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class LostEventChannelsIT extends RedhawkTestBase{
	@Test
	public void test() throws InterruptedException{
		System.out.println("Test to see if Event Channels ever come up");
		try {
			RedhawkEventChannelManager mgr = driver.getDomain().getEventChannelManager();
			while(mgr.getEventChannels().isEmpty()){
				System.out.println("Sleeping for ten seconds while event channels hopefully start up");
				Thread.sleep(10000l);
				mgr.createEventChannel("HelloWorld");
				System.out.println("Created event channel "+mgr.getEventChannels());
				RedhawkEventChannel channel = mgr.getEventChannel("ODM_Channel");
				System.out.println(channel);
				channel = mgr.getEventChannel("HelloWorld");
				System.out.println("Hello "+channel);
			}
			System.out.println("OMG found you!!!!!!");
		} catch (MultipleResourceException | CORBAException | EventChannelCreationException | ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

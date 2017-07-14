package redhawk.testutils;

import org.junit.Test;

import redhawk.driver.eventchannel.RedhawkEventChannelManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.EventChannelCreationException;
import redhawk.driver.exceptions.MultipleResourceException;

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
			}
			System.out.println("OMG found you!!!!!!");
		} catch (MultipleResourceException | CORBAException | EventChannelCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

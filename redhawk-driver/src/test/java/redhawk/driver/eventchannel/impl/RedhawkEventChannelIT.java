package redhawk.driver.eventchannel.impl;

import org.junit.Test;

import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkEventChannelIT extends RedhawkTestBase{
	@Test
	public void testGetRegistrants() throws MultipleResourceException, ResourceNotFoundException, CORBAException{
		RedhawkEventChannelImpl impl = (RedhawkEventChannelImpl) driver.getDomain().getEventChannelManager().getEventChannel("IDM_Channel");
		
		System.out.println(impl.getRegistrants(1000));
	}
}

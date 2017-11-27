package redhawk.rest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redhawk.rest.model.DeviceManager;
import redhawk.rest.model.DeviceManagerContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.Property;
import redhawk.rest.model.SimpleRep;
import redhawk.rest.model.SimpleSequenceRep;
import redhawk.rest.utils.TestUtils;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkRestDeviceManagerIT extends RedhawkTestBase{
	private Logger logger = LoggerFactory.getLogger(RedhawkRestDeviceManagerIT.class);
	
	private static final String NAMESERVER = domainHost+":"+domainPort;
	
	private static RedhawkManager manager = new RedhawkManager(); 
	
	@Test
	public void testGetDeviceManager() {
		try {
			DeviceManagerContainer container = new DeviceManagerContainer(manager.getAll(NAMESERVER, "devicemanager", domainName, FetchMode.EAGER));
			logger.info(TestUtils.getStringFromJAXB(container));
			
			//Ensure something is in container
			assertTrue(!container.getDeviceManagers().isEmpty());
			
			DeviceManager manager = container.getDeviceManagers().get(0);
			
			//Make sure properties exist
			assertTrue(!manager.getProperties().isEmpty());
			
			//Test to make sure the property kind is available
			for(Property prop : manager.getProperties()) {
				if(prop instanceof SimpleRep) {
					assertNotNull(((SimpleRep) prop).getKinds());
				}else if(prop instanceof SimpleSequenceRep) {
					assertNotNull(((SimpleSequenceRep) prop).getKinds());
				}
			}
		} catch (Exception e) {
			fail("Test failure: "+e.getMessage());
		}
	}
}

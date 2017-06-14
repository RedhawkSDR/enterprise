package redhawk.driver.domain.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.domain.RedhawkAllocationManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkAllocationManagerIT extends RedhawkTestBase{
	private static RedhawkAllocationManager allocMgr;
	
	@BeforeClass
	public static void setupAllocationManager(){
		try {
			allocMgr = driver.getDomain().getAllocationManager();
		} catch (MultipleResourceException | CORBAException e) {
			// TODO Auto-generated catch block
			fail("Issue accessing REDHAWK Domain "+e.getMessage());
		}
	}
	
	@Test
	public void testGetCorbaObj(){
		//Test to make sure you can access corba obj from Domain Manager
		assertNotNull(allocMgr.getCorbaObj());
	}
	
	@Test
	public void testListDevices(){
		allocMgr.listDevices();
	}
}

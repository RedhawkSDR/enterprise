package redhawk.driver.domain.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import redhawk.testutils.RedhawkTestBase;

public class RedhawkAllocationManagerIT extends RedhawkTestBase{
	@Test
	public void testGetCorbaObj(){
		//Test to make sure you can access corba obj from Domain Manager
		try {
			assertNotNull(driver.getDomain().getAllocationManager());
		} catch (Exception ex){
			fail("Issue accessing REDHAWK Domain "+ex.getMessage());
		}
	}
}

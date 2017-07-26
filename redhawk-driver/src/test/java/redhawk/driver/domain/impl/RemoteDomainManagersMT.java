package redhawk.driver.domain.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.testutils.RedhawkTestBase;

/*
 * Test this manually since it's an edge case in general users will 
 * have just one Domain. If this changes can look into automating. 
 */
public class RemoteDomainManagersMT extends RedhawkTestBase{
	@Test
	public void testRemoteDomainManager() throws ResourceNotFoundException, CORBAException {
		RedhawkDomainManager mgr = null;
		try {
			mgr = driver.getDomain(domainName);
			
			mgr.registerRemoteDomainManager("REDHAWK_DEV2");
			
			List<Object> remoteDomains = mgr.remoteDomainManagers();
			
			//Should now be 1 remote domain available
			assertEquals(new Integer(1), new Integer(remoteDomains.size()));
			mgr.unregisterRemoteDomainManager("REDHAWK_DEV2");
			
			assertEquals(new Integer(0), new Integer(mgr.remoteDomainManagers().size()));
		} catch (ResourceNotFoundException | CORBAException e) {
			fail("Test failure "+e.getMessage());
		} finally {
			/*
			 * Clear out any leftover remote domains
			 */
			for(String domain : mgr.remoteDomainNames()) {
				mgr.unregisterRemoteDomainManager(domain);
			}
		}
	}
}

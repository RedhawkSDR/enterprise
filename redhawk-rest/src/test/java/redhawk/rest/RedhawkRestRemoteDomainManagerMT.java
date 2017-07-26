/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package redhawk.rest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.rest.model.Domain;
import redhawk.rest.model.RegisterRemoteDomain;
import redhawk.rest.utils.TestUtils;
import redhawk.testutils.RedhawkTestBase;

/*
 * Manual tests for registering remote domain and seeing it's XML
 */
public class RedhawkRestRemoteDomainManagerMT extends RedhawkTestBase{
	private static RedhawkManager manager = new RedhawkManager();

	private static String nameServer; 

	@BeforeClass
	public static void setup(){
		//Launch app 
		nameServer = domainHost+":"+domainPort;
	}
	
	@Test
	public void testRegisterRemoteDomain() throws ResourceNotFoundException, CORBAException {
		String remoteDomainName = "REDHAWK_DEV2";
		RegisterRemoteDomain remoteDomain = new RegisterRemoteDomain(); 
		remoteDomain.setDomainName(remoteDomainName);
		
		try {
			//Test registering a remote domain
			manager.registerRemoteDomain(nameServer, "domain", domainName, remoteDomain);
		
			//driver.getDomain(domainName).registerRemoteDomainManager(remoteDomain.getDomainName());
			//List Remote Domains
			Domain domain = manager.get(nameServer, "domain", domainName);
			assertTrue("Domain Name should not be empty", !domain.getRemoteDomains().isEmpty());
						
			//Make sure response object contains the remote domain
			manager.unregisterRemoteDomain(nameServer, "domain", domainName, remoteDomainName);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Test failure: "+e.getMessage());
		} finally {
			RedhawkDomainManager mgr = driver.getDomain(domainName);
			/*
			 * Clear out any remote domains
			 */
			for(String domain : mgr.remoteDomainNames()) {
				mgr.unregisterRemoteDomainManager(domain);
			}
		}
	}
}

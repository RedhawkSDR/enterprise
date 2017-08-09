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
package redhawk.driver.thread;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import org.junit.Test;

import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.testutils.RedhawkTestBase;

/**
 * Class for testing number of threads being leftover 
 * by driver methods to make sure I'm cleaning up when I can 
 * it's users responsibility to call disconnect when fully 
 * done w/ orb. 
 */
public class CORBAThreadTest extends RedhawkTestBase{	
	
	@Test
	public void testTCDomains() throws InterruptedException {
		Integer originalTC = this.getThreadCount();
		Boolean jacORB = Boolean.valueOf(System.getProperty("jacorb", "false"));
		
		try {
			RedhawkDomainManager domain = driver.getDomain();
			Thread.sleep(1000l);
			
			//Check number of threads after you retrieve a Domain, make sure 
			//it's the expected numbers
			if(jacORB) {
				//ClientMessageReceptor contains thread for connections 
				//to the DomainManager CORBA object
				assertEquals("Expected zero additional thread for DomainManager object", originalTC, this.getThreadCount());				
			}else {
				//SunORB leaves one thread in Idle state outside of DomainManager 
				assertEquals("Expected two additional thread for DomainManager object", new Integer(originalTC+2), this.getThreadCount());
			}
			
			//Ensure you can still do stuff with CORBA object
			assertEquals(domain.getCorbaObj().name(), domainName);
		} catch (MultipleResourceException | CORBAException | InterruptedException e) {
			fail("Unable to run test "+e.getMessage());
		}finally {
			driver.disconnect();
			Thread.sleep(1000l);
			
			//Make sure disconnect cleaned everything up 
			assertEquals("Thread count should be same as original after", originalTC, this.getThreadCount());
		}
	}
	
	@Test
	public void testTCDomain() throws InterruptedException {
		Integer originalTC = this.getThreadCount();
		Boolean jacORB = Boolean.valueOf(System.getProperty("jacorb", "false"));
		try {
			RedhawkDomainManager domain = driver.getDomain(domainName);
			Thread.sleep(1000l);
			
			//Check number of threads after you retrieve a Domain, make sure 
			//it's the expected numbers
			if(jacORB) {
				//ClientMessageReceptor contains thread for connections 
				//to the DomainManager CORBA object
				assertEquals("Expected one additional thread for DomainManager object", originalTC, this.getThreadCount());				
			}else {
				//SunORB leaves one thread in Idle state outside of DomainManager 
				assertEquals("Expected one additional thread for DomainManager object", new Integer(originalTC+2), this.getThreadCount());
			}

			//Ensure you can still do stuff with CORBA object
			assertEquals(domain.getCorbaObj().name(), domainName);
			//System.out.println(this.crunchifyGenerateThreadDump());
		} catch (ResourceNotFoundException | CORBAException | InterruptedException e) {
			fail("Unable to run test "+e.getMessage());
		}finally {
			driver.disconnect();
			Thread.sleep(1000l);
			
			//Make sure disconnect cleaned everything up 
			assertEquals("Thread count should be same as original after", originalTC, this.getThreadCount());
		}
	}

	private static Integer getThreadCount() {
		return ManagementFactory.getThreadMXBean().getThreadCount();
	}
	
	
	/*
	 * Code from crunchify 
	 * https://cdn.crunchify.com/wp-content/uploads/2013/07/Generate-Java-Thread-Dump-Programmatically.png
	 */
	public static String crunchifyGenerateThreadDump() {
		final StringBuilder dump = new StringBuilder(); 
		final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		final ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), 100);
		
		for(ThreadInfo threadInfo : threadInfos) {
			dump.append('"');
			dump.append(threadInfo.getThreadName());
			dump.append("\" ");
			final Thread.State state = threadInfo.getThreadState();
			
			dump.append("\n java.lang.Thread.State: ");
			dump.append(state);
			
			final StackTraceElement[] stackTraceElements = threadInfo.getStackTrace();
			
			for(final StackTraceElement stackTraceElement : stackTraceElements) {
				dump.append("\n \t at");
				dump.append(stackTraceElement);
			}
			
			dump.append("\n\n");
		}
		
		return dump.toString();
	}
}

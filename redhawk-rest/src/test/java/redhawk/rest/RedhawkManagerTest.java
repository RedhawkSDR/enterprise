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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import redhawk.rest.model.FetchMode;

@Ignore("Requires actual RH...")
public class RedhawkManagerTest {
	private RedhawkManager manager; 
	
	@Before
	public void setup(){
		manager = new RedhawkManager(); 
	}
	
	@Test
	public void TestLazyRetrieval() {
		try{
			manager.getAll("localhost:2809", "domain", null, FetchMode.LAZY);
			assertTrue("Passed test!", true);
		}catch(Exception ex){
			fail("Exception doing lazy retrieval of domain ");
			ex.printStackTrace();
		}
	}
	
	@Test
	public void TestEagerRetrieval(){
		try{
			manager.getAll("localhost:2809", "domain", null, FetchMode.EAGER);
			assertTrue("Passed test!", true);
		}catch(Exception ex){
			ex.printStackTrace();
			fail("Exception doing eager retrieval of domain ");
		}
	}
}

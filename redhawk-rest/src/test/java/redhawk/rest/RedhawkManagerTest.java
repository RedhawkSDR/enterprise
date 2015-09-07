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

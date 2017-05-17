package redhawk.rest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RedhawkManagerIT {
	private static RedhawkManager manager; 

	@Test
	public void test() throws Exception{
		manager = new RedhawkManager(); 
		
		//Make sure this method returns properties
		assertEquals("List should not be empty", true, !manager.getProperties("localhost:2809", "application", "REDHAWK_DEV/External.*").getProperties().isEmpty());
	}
}

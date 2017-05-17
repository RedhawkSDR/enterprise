package redhawk.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import redhawk.rest.model.Application;
import redhawk.rest.model.ExternalPort;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.Port;

public class RedhawkManagerIT {
	private static RedhawkManager manager = new RedhawkManager(); 

	@Test
	public void testApplicationWithExternalPortsAndProperties(){
		try {
			List<Application> applications = manager.getAll("localhost:2809", "application", "REDHAWK_DEV", FetchMode.EAGER);
		
			System.out.println(applications);
			assertEquals("Should be atleast 1 app", true, applications.size()>=1);
			assertEquals("Properties should be here", true, !applications.isEmpty());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testApplicationWithExternalPortsAndProperties2(){
		try {
			Application application = manager.get("localhost:2809", "application", "REDHAWK_DEV/External.*");
		
			assertNotNull("Should be 1 app", application);
			assertEquals("Properties should be here", true, !application.getProperties().isEmpty());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testApplicationProperties() throws Exception{
		
		//Make sure this method returns properties
		assertEquals("List should not be empty", true, !manager.getProperties("localhost:2809", "application", "REDHAWK_DEV/External.*").getProperties().isEmpty());
	
		//TODO: Add more asserts
	}
	
	@Test
	public void testApplicationPorts(){
		try {
			List<Port> ports = manager.getAll("localhost:2809", "applicationport", "REDHAWK_DEV/External.*", FetchMode.EAGER);
			
			assertEquals("Should be three external ports", 3, ports.size());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testApplicationPort(){
		try {
			ExternalPort port = manager.get("localhost:2809", "applicationport", "REDHAWK_DEV/External.*", "sigGenPort");
			
			System.out.println(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}

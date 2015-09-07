package redhawk.driver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.ORBPackage.InvalidName;

import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkDriverTestIT {
	private String domainName;
	
	private String hostName; 
	
	private int domainPort;
	
	private RedhawkDriver driver; 
	
	@Before
	public void setup(){
		domainName = "REDHAWK_DEV";
		hostName = "localhost";
		domainPort = 2809;
	}
	
	@Test
	public void testDefaultConstructor() throws CORBAException, ResourceNotFoundException{
		driver = new RedhawkDriver();
		
		this.basicDriverTests(driver);
	}
	
	@Test
	public void testOneArgConstructor() throws CORBAException, ResourceNotFoundException{
		driver = new RedhawkDriver(hostName);
		
		this.basicDriverTests(driver);
	}
	
	@Test
	public void testTwoArgConstructor() throws CORBAException, ResourceNotFoundException{
		driver = new RedhawkDriver(hostName, domainPort);
		
		this.basicDriverTests(driver);
	}
	
	
	
	/*Come back to this 
	 * @Test
	public void testGetDominFailures() throws ResourceNotFoundException{
		driver = new RedhawkDriver(); 
		
	}
	*/
	
	private void basicDriverTests(RedhawkDriver driver) throws CORBAException, ResourceNotFoundException{
		//Ensure default port and host are returned correctly
		assertEquals(hostName, driver.getHostName());
		assertEquals(domainPort, driver.getPort());
		
		Map<String, RedhawkDomainManager> domainMap = driver.getDomains();
		
		//Ensure expected Number of domains returned and correct domain name 
		assertEquals(1, domainMap.size());
		assertEquals(domainName, domainMap.keySet().iterator().next());
		
		RedhawkDomainManager domain = driver.getDomain(domainName);
		assertNotNull(domain);
		assertEquals(domainName, domain.getName());
	}
	
	@After
	public void shutdown(){
		driver.disconnect();
		//TODO: Ask John why getOrb initializes an Orb??? 
		//assertEquals(null, driver.getOrb());
	}
}

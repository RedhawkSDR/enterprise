package redhawk.driver.domain.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import CF.DomainManager;
import redhawk.RedhawkTestBase;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.devicemanager.RedhawkService;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;

@Ignore("Will not work until REDHAWKTestBase is setup proper. That either means giving it an actual RHDriver object connected to a domain "
		+ "or mocking everything the right way...")
public class RedhawkDomainManagerImplTest extends RedhawkTestBase {

	private RedhawkDomainManager domainManager;
	
	@Before
	public void setup2() throws ConnectionException, ResourceNotFoundException, CORBAException, MultipleResourceException, IOException {
		super.setup();
		domainManager = redhawk.getDomain("REDHAWK_DEV");
	}
	
	@Test
	public void testGetName() throws ConnectionException, ResourceNotFoundException, CORBAException {
		Assert.assertEquals(domainManager.getName(), "REDHAWK_DEV");;
	}

	@Test
	public void testGetIdentifier() throws ConnectionException, ResourceNotFoundException, CORBAException {
		Assert.assertEquals(domainManager.getIdentifier(), "DCE:9ae444e0-0bfd-4e3d-b16c-1cffb3dc0f46");
	}

	@Test
	public void testGetCorbaObj() throws ConnectionException, ResourceNotFoundException, CORBAException {
		Assert.assertNotNull(domainManager.getCorbaObj());
		Assert.assertTrue(domainManager.getCorbaObj() instanceof DomainManager);
	}

	@Test
	public void testGetFileManager() throws ConnectionException, ResourceNotFoundException, CORBAException {
		Assert.assertNotNull(domainManager.getFileManager());
	}	

	@Test
	public void testGetDriver() throws ConnectionException, ResourceNotFoundException, CORBAException {
		Assert.assertNotNull(domainManager.getDriver());
	}

	@Test
	public void testGetDeviceManagers() throws ConnectionException, ResourceNotFoundException, CORBAException {
		List<RedhawkDeviceManager> deviceManagers = domainManager.getDeviceManagers();
		Assert.assertTrue(deviceManagers.size() > 0);
	}

	@Test
	public void testApplications() throws ConnectionException, ResourceNotFoundException, CORBAException {
		Map<String, RedhawkApplication> apps = domainManager.applications();
		Assert.assertTrue(apps.size() > 0);
	}
	
	@Test
	public void testDeviceManagers() throws ConnectionException, ResourceNotFoundException, CORBAException {
		Map<String, RedhawkDeviceManager> deviceManagers = domainManager.deviceManagers();
		Assert.assertTrue(deviceManagers.size() > 0);
	}

	@Test
	public void testGetDeviceManagerByIdentifier() throws ConnectionException, ResourceNotFoundException, CORBAException {
		RedhawkDeviceManager deviceManager = domainManager.getDeviceManagerByIdentifier("DCE:a2e27463-f8ef-4d1c-935b-ab9acd1512a2");
		Assert.assertNotNull(deviceManager);
	}

	@Test(expected=ResourceNotFoundException.class)
	public void testGetDeviceManagerByIdentifierNotFound() throws ConnectionException, ResourceNotFoundException, CORBAException {
		RedhawkDeviceManager deviceManager = domainManager.getDeviceManagerByIdentifier("DCE");
		Assert.assertNotNull(deviceManager);
	}
	
	@Test
	public void testCreateDeviceManager() throws Exception {
		domainManager.createDeviceManager("TEST", "/tmp", false);
		Assert.assertNotNull(domainManager.getDeviceManagerByName("TEST.*"));
		domainManager.unRegisterAllDriverRegisteredDeviceManagers();
	}
	
	@Test
	public void testCreateDurableDeviceManager() throws Exception {
		domainManager.createDeviceManager("TEST", "/tmp", true);
		Assert.assertNotNull(domainManager.getDeviceManagerByName("TEST.*"));
		domainManager.unRegisterAllDriverRegisteredDeviceManagers();
	}
	
	@Test
	public void testGetDevices() throws Exception {
		List<RedhawkDevice> devices = domainManager.getDevices();
		Assert.assertTrue(devices.size() > 0);
		Map<String, RedhawkDevice> deviceMap = domainManager.devices();
		Assert.assertTrue(deviceMap.size() > 0);
	}

	@Test
	public void testToString() throws Exception {
		Assert.assertEquals(domainManager.toString(), "RedhawkDomainManagerImpl [domainName=REDHAWK_DEV, uniqueIdentifier=DCE:9ae444e0-0bfd-4e3d-b16c-1cffb3dc0f46]");
	}

	@Test
	public void testGetDevicesByName() {
		List<RedhawkDevice> devices = domainManager.getDevicesByName("GPP.*");
		Assert.assertTrue(devices.size() > 0);
	}
	
	@Test
	public void testGetDeviceByName() throws MultipleResourceException {
		RedhawkDevice device = domainManager.getDeviceByName("GPP.*");
		Assert.assertTrue(device.getName().startsWith("GPP"));
	}
	
	@Test
	public void testGetDeviceByIdentifier() throws MultipleResourceException {
		RedhawkDevice device = domainManager.getDeviceByIdentifier("DCE:9ae444e0-0bfd-4e3d-b16c-1cffb3dc0f46");
		Assert.assertTrue(device.getName().startsWith("GPP"));
	}
	
	@Test
	public void testGetServices()  {
		List<RedhawkService> services = domainManager.getServices();
		Assert.assertNotNull(services);
	}
	
	@Test
	public void testGetServicesByName()  {
		List<RedhawkService> services = domainManager.getServicesByName("TEST");
		
	}
	
	
//    RedhawkDeviceManager getDeviceManagerByName(String name) throws MultipleResourceException, ResourceNotFoundException;
//    List<RedhawkDevice> getDevices();
// 	List<RedhawkDevice> getDevicesByName(String deviceName);
// 	RedhawkDevice getDeviceByName(String deviceName) throws MultipleResourceException;
// 	RedhawkDevice getDeviceByIdentifier(String identifier);
//	List<RedhawkService> getServices();
//	List<RedhawkService> getServicesByName(String serviceName);
//	RedhawkService getServiceByName(String serviceName) throws MultipleResourceException;
//	RedhawkApplication createApplication(String instanceName, File sadFile) throws ApplicationCreationException;
//	RedhawkApplication createApplication(String instanceName, Softwareassembly softwareAssembly) throws ApplicationCreationException;
//	RedhawkApplication createApplication(String instanceName, String sadFileDestination) throws ApplicationCreationException;
//    List<RedhawkApplication> getApplications();
//    List<RedhawkApplication> getApplicationsByName(String name);
//    RedhawkApplication getApplicationByName(String name) throws MultipleResourceException, ResourceNotFoundException;
//    RedhawkApplication getApplicationByIdentifier(String identifier) throws ResourceNotFoundException;
//    RedhawkDeviceManager createDeviceManager(String deviceManagerName, String fileSystemRoot, boolean durable) throws Exception;
//    RedhawkDeviceManager createDeviceManager(String deviceManagerName, String fileSystemRoot, boolean durable, DeviceManagerInturruptedCallback callback) throws Exception;
//    Map<String, RedhawkDeviceManager> getDriverRegisteredDeviceManagers();
//    void unRegisterAllDriverRegisteredDeviceManagers();
//	RedhawkConnectionManager getConnectionManager();
//    RedhawkAllocationManagerImpl getAllocationManager();
//    RedhawkEventChannelManager getEventChannelManager();
//	RedhawkApplicationFactoryImpl getApplicationFactoryByIdentifier(String identifier) throws ResourceNotFoundException, MultipleResourceException;
	
}

package redhawk.driver;

import java.util.Map;

import org.omg.CORBA.ORB;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;

public interface Redhawk {

    void disconnect();
    ORB getOrb();

    String getHostName();
    int getPort();
    
	Map<String, RedhawkDomainManager> getDomains() throws CORBAException;
	RedhawkDomainManager getDomain(String domainName) throws ConnectionException, ResourceNotFoundException, CORBAException;
	
    RedhawkApplication getApplication(String applicationLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    RedhawkComponent getComponent(String componentLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    RedhawkPort getPort(String portLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    RedhawkDevice getDevice(String deviceLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    RedhawkDeviceManager getDeviceManager(String deviceManagerLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    
}
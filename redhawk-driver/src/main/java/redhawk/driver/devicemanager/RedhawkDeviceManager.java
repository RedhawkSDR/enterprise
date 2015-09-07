package redhawk.driver.devicemanager;

import java.util.List;

import CF.DeviceManager;
import redhawk.driver.base.QueryableResource;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.exceptions.ServiceRegistrationException;

public interface RedhawkDeviceManager extends QueryableResource {


    List<RedhawkDevice> getDevices();
    RedhawkDevice getDeviceByName(String name) throws ResourceNotFoundException;
    RedhawkDevice getDeviceByIdentifier(String identifier) throws ResourceNotFoundException;
    String getName();
    String getUniqueIdentifier();
    RedhawkDomainManager getDomainManager();
    List<RedhawkService> getServices();
    RedhawkService getServiceByName(String name) throws MultipleResourceException, ResourceNotFoundException;

    RedhawkDeviceManagerFileSystem getFileSystem();
    DeviceManager getCorbaObject();
    RedhawkDeviceManagerFileSystem getDeviceManagerFileSystem();

    void registerService(String serviceName, java.lang.Object objectToRegister, Class poaTieClass, Class operationsInterface) throws ServiceRegistrationException;
    void unregisterService(String serviceName) throws MultipleResourceException, ResourceNotFoundException;
    
    void shutdown();
}

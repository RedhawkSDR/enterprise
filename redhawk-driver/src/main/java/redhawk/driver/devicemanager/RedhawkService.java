package redhawk.driver.devicemanager;

import CF.DeviceManagerPackage.ServiceType;

public interface RedhawkService {
    ServiceType getCorbaObj();
    RedhawkDeviceManager getDeviceManager();
    
    public String getServiceName();
    public Object getServiceObject();

}

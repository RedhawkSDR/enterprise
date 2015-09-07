package redhawk.driver.devicemanager.impl;

import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.devicemanager.RedhawkService;
import CF.DeviceManagerPackage.ServiceType;

public class RedhawkServiceImpl implements RedhawkService {

    private RedhawkDeviceManager deviceManager;
    private ServiceType service;
    
    public RedhawkServiceImpl(RedhawkDeviceManager deviceManager, ServiceType service) {
        this.deviceManager = deviceManager;
        this.service = service;
    }

    public String getServiceName(){
        return service.serviceName;
    }
    
    public Object getServiceObject(){
        return service.serviceObject;
    }
    
    public ServiceType getCorbaObj(){
        return service;
    }
    
    public RedhawkDeviceManager getDeviceManager(){
        return deviceManager;    
    }


}
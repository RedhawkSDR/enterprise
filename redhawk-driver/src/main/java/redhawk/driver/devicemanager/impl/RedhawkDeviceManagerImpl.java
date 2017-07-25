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
package redhawk.driver.devicemanager.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

import CF.Device;
import CF.DeviceManager;
import CF.DeviceManagerHelper;
import CF.InvalidObjectReference;
import CF.Resource;
import CF.ResourceHelper;
import redhawk.driver.base.impl.QueryableResourceImpl;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.device.impl.RedhawkDeviceImpl;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.devicemanager.RedhawkDeviceManagerFileSystem;
import redhawk.driver.devicemanager.RedhawkService;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.exceptions.ServiceRegistrationException;
import redhawk.driver.logging.RedhawkLogLevel;
import redhawk.driver.properties.RedhawkSimple;

public class RedhawkDeviceManagerImpl extends QueryableResourceImpl<DeviceManager> implements RedhawkDeviceManager  {

	private static Logger logger = Logger.getLogger(RedhawkDeviceManagerImpl.class.getName());
    protected RedhawkDomainManager domainManager;
    protected String identifier;
    
    public RedhawkDeviceManagerImpl(RedhawkDomainManager domainManager, String deviceManagerIor, String identifier){
    	super(deviceManagerIor, domainManager.getDriver().getOrb());
        this.setDomainManager(domainManager);
        this.identifier = identifier;
    }
    
    public void shutdown() {
		/*
		 * Check to see if this was a driver registered device manager. 
		 * If so remove from the driver list
		 */
		String key = domainManager.getName()+":"+this.getName();
		
		//If key exist remove from list
		if(domainManager.getDriverRegisteredDeviceManagers().containsKey(key)){
			domainManager.getDriverRegisteredDeviceManagers().remove(key);
		}
		
		this.getCorbaObject().shutdown();
    }
    
    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RedhawkDeviceManagerImpl [name=").append(getName()).append(", uniqueIdentifier=").append(getUniqueIdentifier()).append("]");
		return builder.toString();
	}

    private RedhawkDevice createRedhawkDevice(Device device, RedhawkDeviceManager deviceManager, String deviceIor, String identifier) {
    	RedhawkDevice dev = new RedhawkDeviceImpl(this, getOrb().object_to_string(device), device.identifier());
    	
    	//TODO: Why is this happening
    	RedhawkSimple dev_kind = dev.getProperty("DCE:cdc5ee18-7ceb-4ae6-bf4c-31f983179b4d");
    	if ((dev_kind != null) && (dev_kind.getValue() != null)) {
	    	String kind = dev_kind.getValue().toString();
	    	// there are many kinds of devices, FRONTEND, GPP, and more, each with its own methods of allocation and properties
	    	// FRONTEND specifically has a hierarchy of device types as well with FRONTEND::TUNER and other devices that are not
	    	// specifically handled here
	    	if ((kind != null) && kind.startsWith("FRONTEND")) {
//	    		return new RedhawkFrontendDeviceImpl(this, getOrb().object_to_string(device), device.identifier());
	    	}
    	}
    	return dev;
    }
    
	public List<RedhawkDevice> getDevices(){
		return Arrays.stream(getCorbaObject().registeredDevices()).map(device -> createRedhawkDevice(device, this, getOrb().object_to_string(device), device.identifier())).collect(Collectors.toList());
    }

	@Override
	protected DeviceManager locateCorbaObject() throws ResourceNotFoundException {
		String ior = ((RedhawkDeviceManagerImpl) domainManager.getDeviceManagerByIdentifier(identifier)).getIor();
		return DeviceManagerHelper.narrow(getOrb().string_to_object(ior));
	}	
	
    
    public RedhawkDeviceManagerFileSystem getFileSystem(){
        return new RedhawkDeviceManagerFileSystemImpl(getCorbaObject().fileSys());
    }
    
    
    public RedhawkDevice getDeviceByName(String name) throws ResourceNotFoundException{
        for(Device device : getCorbaObject().registeredDevices()){
            if(device.label().toLowerCase().matches(name.toLowerCase())){
                return createRedhawkDevice(device, this, getOrb().object_to_string(device), device.identifier());
            }
        }
        
        throw new ResourceNotFoundException("Could not find a device with the name: " + name);
    }
    
    public RedhawkDevice getDeviceByIdentifier(String identifier) throws ResourceNotFoundException{
        for(Device device : getCorbaObject().registeredDevices()){
            if(device.identifier().toLowerCase().matches(identifier.toLowerCase())){
                return createRedhawkDevice(device, this, getOrb().object_to_string(device), device.identifier());
            }
        }
        
        throw new ResourceNotFoundException("Could not find a device with the identifier: " + identifier);
    }    

    
    public List<RedhawkService> getServices(){
    	return Arrays.stream(getCorbaObject().registeredServices()).map(s -> new RedhawkServiceImpl(this, s)).collect(Collectors.toList());
    }
    
    public RedhawkService getServiceByName(String name) throws MultipleResourceException, ResourceNotFoundException{
    	List<RedhawkService> services = getServices().stream().filter(s -> {return s.getServiceName().toLowerCase().matches(name.toLowerCase());}).collect(Collectors.toList());
        if(services.size() > 1){
        	throw new MultipleResourceException("Multiple Services were found with the name: "+ name);
        } else if(services.size() == 1){
        	return services.get(0);
        } else {
        	throw new ResourceNotFoundException("Could not find service with the name of: "+ name);
        }
    }
    
    public String getName(){
        return getCorbaObject().label();
    }

    public String getUniqueIdentifier(){
        return getCorbaObject().identifier();
    }
    
    public RedhawkDomainManager getDomainManager() {
        return domainManager;
    }
    
    
    public void setDomainManager(RedhawkDomainManager domainManager) {
        this.domainManager = domainManager;
    }

    
    public RedhawkDeviceManagerFileSystem getDeviceManagerFileSystem(){
    	return new RedhawkDeviceManagerFileSystemImpl(getCorbaObject().fileSys());
    }

	@Override
	public Class<?> getHelperClass() {
		return CF.DeviceManagerHelper.class;
	}


	@Override
	public void registerService(String serviceName, java.lang.Object objectToRegister, Class poaTieClass, Class operationsInterface) throws ServiceRegistrationException {
    	
		try {
			POA rootPOA = POAHelper.narrow(getOrb().resolve_initial_references("RootPOA"));
	        rootPOA.the_POAManager().activate();   
	        ClassLoader classloader = this.getClass().getClassLoader();
	        
	        Constructor c = Class.forName(poaTieClass.getName(), true, classloader).getConstructor(Class.forName(operationsInterface.getName(), true, classloader));
	        java.lang.Object poaTie = (java.lang.Object)c.newInstance(objectToRegister);
	        Method meth = poaTie.getClass().getSuperclass().getMethod("_this", ORB.class);
	        org.omg.CORBA.Object serviceObject = (Object)meth.invoke(poaTie, getOrb());    	  
	        getCorbaObject().registerService(serviceObject, serviceName);
	        
	        Object objRef = getOrb().resolve_initial_references("NameService");
	        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	        
	        String service = domainManager.getName()+"/"+serviceName;
	        service = service.replaceAll("\\.", "\\\\.");
	        
	        NameComponent[] serviceNameComponent = null;
	        try {
	        	serviceNameComponent = ncRef.to_name(service);
	        	ncRef.bind(serviceNameComponent, serviceObject);
	        } catch(AlreadyBound e) {
	        	if(serviceNameComponent != null){
	        		try {
						ncRef.rebind(serviceNameComponent, serviceObject);
					} catch (NotFound | CannotProceed e1) {
						logger.log(Level.SEVERE, "A CORBA Exception has occurred." , e1);
					}
	        	}
	        } catch (NotFound e) {
	        	logger.log(Level.SEVERE, "A CORBA Exception has occurred." , e);
			} catch (CannotProceed e) {
				logger.log(Level.SEVERE, "A CORBA Exception has occurred." , e);
			}
		} catch (InvalidName e){
			logger.log(Level.SEVERE, "A CORBA Exception has occurred." , e);
			throw new ServiceRegistrationException("A CORBA Exception has occurred." , e);
		} catch (org.omg.CORBA.ORBPackage.InvalidName | InvalidObjectReference | AdapterInactive e1) {
			logger.log(Level.SEVERE, "A CORBA Exception has occurred." , e1);
			throw new ServiceRegistrationException("A CORBA Exception has occurred." , e1);
		} catch (ConnectionException e1) {
			logger.log(Level.SEVERE, "A ConnectionException has occurred." , e1);
			throw new ServiceRegistrationException("A ConnectionException has occurred." , e1);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException |
				IllegalArgumentException | InvocationTargetException e1) {
			logger.log(Level.SEVERE, "A java Reflection error has occurred." , e1);
			throw new ServiceRegistrationException("A java Reflection error has occurred." , e1);
		}
	}


	@Override
	public void unregisterService(String serviceName) throws MultipleResourceException, ResourceNotFoundException {
		try {
			getCorbaObject().unregisterService(getServiceByName(serviceName).getCorbaObj().serviceObject, serviceName);
		} catch (InvalidObjectReference e) {
			logger.log(Level.SEVERE, "An InvalidObjectReference error has occurred." , e);
		} catch (ConnectionException e) {
			logger.log(Level.SEVERE, "An ConnectionException error has occurred." , e);
		}
	}

	@Override
	public RedhawkLogLevel getLogLevel() {
		throw new UnsupportedOperationException("DeviceManager does not implemnt the CORBA Logging interface.");
	}

	@Override
	public void setLogLevel(RedhawkLogLevel level) {
		throw new UnsupportedOperationException("DeviceManager does not implement the CORBA Logging interface.");		
	}

	@Override
	public String deviceConfigurationProfile() {
		return this.getCorbaObject().deviceConfigurationProfile();
	}

	@Override
	public String getComponentImplemantation() throws MultipleResourceException {
		//Should only be one device per deviceManager
		RedhawkDevice rhDevice = null;
		for(RedhawkDevice device : this.getDevices()) {
			if(rhDevice==null)
				rhDevice = device;
			else
				throw new MultipleResourceException("Get Implementation from specific device since multipe devices are "
						+ "available for this DeviceManager");
		}
		
		return this.getCorbaObject().getComponentImplementationId(rhDevice.getIdentifier());
	}
}
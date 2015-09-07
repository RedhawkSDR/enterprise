package redhawk.driver.devicemanager.impl;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.CORBA.Object;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import CF.DeviceManager;
import CF.DeviceManagerHelper;
import CF.DeviceManagerPackage.ServiceType;
import redhawk.driver.devicemanager.DeviceManagerInturruptedCallback;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.exceptions.ServiceRegistrationException;

public class DurableRedhawkDeviceManagerImpl extends RedhawkDeviceManagerImpl  {

	private static Logger logger = Logger.getLogger(DurableRedhawkDeviceManagerImpl.class.getName());
    private Timer deviceManagerMonitor = new Timer();
	
	private DeviceManagerInturruptedCallback callback;
	
	private Map<String, java.lang.Object[]> registeredServices = new HashMap<>();
	
	private boolean lostDomain = false;
	
	public DurableRedhawkDeviceManagerImpl(final String domainName, final String deviceManagerName, final String fileSystemRoot, final RedhawkDomainManager domainManager, String deviceManagerIor, String identifier, DeviceManagerInturruptedCallback callback){
		this(domainName, deviceManagerName, fileSystemRoot, domainManager, deviceManagerIor, identifier);
		this.callback = callback;
	}
	
	
    public DurableRedhawkDeviceManagerImpl(final String domainName, final String deviceManagerName, final String fileSystemRoot, final RedhawkDomainManager domainManager, String deviceManagerIor, String identifier){
    	super(domainManager, deviceManagerIor, identifier);
    	
        Runtime.getRuntime().addShutdownHook(new Thread() {
	        public void run() {
				shutdown();
	        }
        });
        
        
		TimerTask deviceManagerMonitorTask = new TimerTask() {
			@Override
			public void run() {
				try {
					Object objRef = getOrb().resolve_initial_references("NameService");
					NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
				    String host = deviceManagerName;
				    try {
			            logger.log(Level.FINE, "Attempting to get the local host name");
			            host = deviceManagerName+"_" + Inet4Address.getLocalHost().getCanonicalHostName();
			            if ("localhost".equalsIgnoreCase(host)) {
			                logger.log(Level.FINE, "Hostname is Localhost");    
			                host = deviceManagerName;
			            }
				    } catch (UnknownHostException e1) {
				        logger.log(Level.WARNING, "UnknownHostException when trying to compute the hostname for the redbus device manager", e1);
				    } catch (Exception e){
				    	logger.log(Level.SEVERE, "PROBLEM COMPUTING HOSTNAME FOR COMPUTER", e);
				    }
					
                    String name = domainName+"/"+ host;
                    name = name.replaceAll("\\.", "\\\\.");
                    
                    try {
                    	domainManager.getCorbaObj().identifier();
                    	
                    	if(lostDomain){
                    		
                    		try {
                    			DeviceManager devMgr = DeviceManagerHelper.narrow(ncRef.resolve_str(name));
                            	setIor(getOrb().object_to_string(devMgr));
                            	
                            	for(String service : registeredServices.keySet()){
                            		try {
                            			java.lang.Object[] serviceInformation = registeredServices.get(service);
                            			boolean found = false;
                            			for(ServiceType serviceType : devMgr.registeredServices()){
                            				if(serviceType.serviceName.equals(service)){
                            					found = true;
                            				}
                            			}
                            			
                            			if(!found){
                            				try {
	                            				domainManager.getCorbaObj().unregisterService((Object) serviceInformation[3], service);
                            				} catch(Exception e){
                            					//do nothing
                            				}
                            			}
                            			
                            			if(!found) {
                            				registerService(service, serviceInformation[0], (Class) serviceInformation[1], (Class) serviceInformation[2]);
                            			}
                            			
                            		} catch(ServiceRegistrationException e1){
                            			logger.log(Level.SEVERE, "A problem occurred while trying to register service: " + service, e1);
                            		}
                            	}                   	
                    		} catch(Throwable t){
                    			new DeviceManagerTemplate(domainName, domainManager.getCorbaObj(), getOrb(), deviceManagerName, fileSystemRoot);
                    		}
                    	}
                    	
                    } catch(Exception e){
                    	
                    	if(!lostDomain){
                    		lostDomain = true;
                    		if(callback != null){
                    			callback.lostDomainManagerConnection();
                    		}
                    	}
                    }

				} catch (Throwable e) {
					if(callback != null){
                		callback.connectionProblem(e.getMessage());
                	}
					e.printStackTrace();
                    logger.log(Level.INFO, "There was a problem when attempting to connect to the REDHAWK Domain: "+ domainName +" ", e);
				}
			}
		};
	
		deviceManagerMonitor.scheduleAtFixedRate(deviceManagerMonitorTask, 1000, 5000);
    }
    
    @Override
	public void shutdown() {
    	if(deviceManagerMonitor != null){
    		logger.info("====================== SHUTTING DOWN =========================== " );
    		deviceManagerMonitor.cancel();
    		deviceManagerMonitor.purge();
    		deviceManagerMonitor = null;
    	}
    	super.shutdown();
	}

	@Override
	public void registerService(String serviceName, java.lang.Object objectToRegister, Class poaTieClass, Class operationsInterface) throws ServiceRegistrationException {
		super.registerService(serviceName, objectToRegister, poaTieClass, operationsInterface);
		try {
			Object objRef = getOrb().resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			Object serviceObject = ncRef.resolve_str(domainManager.getName()+"/"+serviceName);
			java.lang.Object[] serviceInformation = new java.lang.Object[]{objectToRegister, poaTieClass, operationsInterface, serviceObject};
			registeredServices.put(serviceName, serviceInformation);
		} catch(Exception e){
			logger.log(Level.SEVERE, "THERE WAS A PROBLEM REGISTERING THE SERVICE: ", e);
		}
	}


	@Override
	public void unregisterService(String serviceName) throws MultipleResourceException, ResourceNotFoundException {
		super.unregisterService(serviceName);
		registeredServices.remove(serviceName);
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DurableRedhawkDeviceManagerImpl [name=")
				.append(getName()).append(", uniqueIdentifier=")
				.append(getUniqueIdentifier()).append("]");
		return builder.toString();
	}


}
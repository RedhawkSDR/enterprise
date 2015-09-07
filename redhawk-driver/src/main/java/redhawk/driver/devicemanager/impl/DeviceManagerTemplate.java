package redhawk.driver.devicemanager.impl;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.InterfaceDef;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.PolicyListHolder;
import org.omg.CORBA.Request;
import org.omg.CORBA.SetOverrideType;
import org.omg.CORBA.TRANSIENT;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.WrongAdapter;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import CF.DataType;
import CF.Device;
import CF.DeviceManager;
import CF.DeviceManagerHelper;
import CF.DeviceManagerPOATie;
import CF.DomainManager;
import CF.File;
import CF.FilePOATie;
import CF.FileSystem;
import CF.FileSystemPOATie;
import CF.InvalidIdentifier;
import CF.InvalidObjectReference;
import CF.InvalidProfile;
import CF.PropertiesHolder;
import CF.UnknownProperties;
import CF.DeviceManagerPackage.ServiceType;
import CF.DomainManagerPackage.DeviceManagerNotRegistered;
import CF.DomainManagerPackage.RegisterError;
import CF.DomainManagerPackage.UnregisterError;
import CF.PortSetPackage.PortInfoType;
import CF.PortSupplierPackage.UnknownPort;
import CF.PropertyEmitterPackage.AlreadyInitialized;
import CF.PropertySetPackage.InvalidConfiguration;
import CF.PropertySetPackage.PartialConfiguration;

public class DeviceManagerTemplate implements DeviceManager {

	private FileSystem fileSys;
	
	private static Logger logger = Logger.getLogger(DeviceManagerTemplate.class.getName());
	
	private String componentImplementationId = "DCE:"+UUID.randomUUID().toString();
	private String identifier; // = "DCE:"+UUID.randomUUID().toString();
	private List<ServiceType> registeredServices = new CopyOnWriteArrayList<ServiceType>();
	private List<Device> registeredDevices =  new CopyOnWriteArrayList<Device>();

	private DomainManager domainManager;
	private DeviceManagerPOATie deviceManagerTie;
	private String deviceManagerName;
	
	private FileSystemPOATie fstie;
	private FilePOATie rbFileTie;
	private File actualFile;
	
	private ORB orb;
	private String domainName;
	private String fileSystemRoot;
	private DeviceManager corbaDeviceManager;
	
	public DeviceManagerTemplate(String domainName, DomainManager domainManager, ORB orb, String deviceManagerName, String fileSystemRoot) throws Exception {
	    this.domainManager = domainManager;
	    this.deviceManagerName = deviceManagerName;
	    this.orb = orb;
	    this.fileSystemRoot = fileSystemRoot;
	    this.domainName = domainName;
	    this.identifier = domainName+":"+deviceManagerName;
	    initializeFileSystem();
	    internalCreate();
	}
	
	private void initializeFileSystem() {
		try {
	        DcdFile dcdFile = new DcdFile(domainName, deviceManagerName);
	        dcdFile.setIdentifier(identifier);
	        rbFileTie = new FilePOATie(dcdFile);
	        actualFile = rbFileTie._this(orb);
	        POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
	        rootPOA.the_POAManager().activate();
	        StandardFileSystemOperations fileSystemOperations = new StandardFileSystemOperations(orb, rootPOA, new java.io.File(fileSystemRoot));
	        fileSystemOperations.setDcdFile(actualFile);
	        fstie = new FileSystemPOATie(fileSystemOperations);                
	        FileSystem fs = fstie._this(orb);
	        fileSys = fs;
		} catch(org.omg.CORBA.ORBPackage.InvalidName e){
			logger.log(Level.SEVERE, "Could Not Initialize Device Manager File System: " ,e);
		} catch (AdapterInactive e) {
			e.printStackTrace();
		}
	}
	
	
	private void internalCreate() throws Exception {
		Object objRef = orb.resolve_initial_references("NameService");
	    final NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	    POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
        rootPOA.the_POAManager().activate();
        DeviceManagerPOATie deviceManagerTie = new DeviceManagerPOATie(this);
        final DeviceManager deviceManager = deviceManagerTie._this(orb);
        setCorbaDeviceManager(deviceManager);
        setDeviceManagerTie(deviceManagerTie);
        String name = domainName+"/"+label();
        name = name.replaceAll("\\.", "\\\\.");
        final NameComponent[] deviceMgrName = ncRef.to_name(name);
        
        try {
        	ncRef.rebind(deviceMgrName, deviceManager);
        	boolean found = false;
        	for(DeviceManager devMgr : domainManager.deviceManagers()){
        		if(devMgr.label().equalsIgnoreCase(deviceManager.label())){
        			found = true;
        		}
        	}
        	
        	if(!found){
        		domainManager.registerDeviceManager(deviceManager);
        	}
        	
        } catch(Exception e){
        	
        	try {
	        	DeviceManager d = DeviceManagerHelper.narrow(ncRef.resolve_str(name));
	        	d.identifier();
        	} catch (TRANSIENT t){
	        	DeviceManagerPOATie devManTie = new DeviceManagerPOATie(new DummyDeviceManagerForCleanupOnly(identifier));
	            final DeviceManager dM = devManTie._this(orb);
        		domainManager.unregisterDeviceManager(dM);
        	}
        	
        	domainManager.registerDeviceManager(deviceManager);
        }
        
	}
	
	
	
	@Override
	public void configure(DataType[] arg0) throws InvalidConfiguration, PartialConfiguration {
		
	}

	@Override
	public void query(PropertiesHolder arg0) throws UnknownProperties {
		
	}

	@Override
	public Object getPort(String arg0) throws UnknownPort {
		return null;
	}

	@Override
	public String deviceConfigurationProfile() {
		return "DeviceManager.dcd.xml";
	}

	@Override
	public FileSystem fileSys() {
		return fileSys;
	}

	@Override
	public String getComponentImplementationId(String arg0) {
		return componentImplementationId;
	}

	@Override
	public String identifier() {
		return identifier;
	}

	@Override
	public String label() {
	    String host = deviceManagerName;
	    try {
            logger.fine("Attempting to get the local host name");
            host = deviceManagerName+"_" + Inet4Address.getLocalHost().getCanonicalHostName();
            if ("localhost".equalsIgnoreCase(host)) {
                logger.fine("Hostname is Localhost");    
                host = deviceManagerName;
            }
	    } catch (UnknownHostException e1) {
	        logger.log(Level.WARNING, "UnknownHostException when trying to compute the hostname for the redbus device manager", e1);
	    } catch (Exception e){
	    	logger.log(Level.SEVERE, "PROBLEM COMPUTING HOSTNAME FOR COMPUTER", e);
	    }
	                
	    return host;	    
	}

	
	@Override
	public Device[] registeredDevices() {
		synchronized (registeredDevices) {
			return registeredDevices.toArray(new Device[registeredDevices.size()]);
		}
	}

	@Override
	public ServiceType[] registeredServices() {
		synchronized (registeredServices) {
			return registeredServices.toArray(new ServiceType[registeredServices.size()]);
		}
	}

	@Override
	public void shutdown() {

		synchronized (registeredServices) {
			for(ServiceType type : registeredServices){
				registeredServices.remove(type);
				try {
					domainManager. unregisterService(type.serviceObject, type.serviceName);
 	                Object objRef = orb.resolve_initial_references("NameService");
	                NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	                String serviceName = domainName+"/"+type.serviceName;
	                serviceName = serviceName.replaceAll("\\.", "\\\\.");
	                NameComponent[] serviceNameComponent = ncRef.to_name(serviceName);
	                ncRef.unbind(serviceNameComponent);
				} catch (UnregisterError e) {
					logger.log(Level.SEVERE, "A UnregisterError error has occurred. ", e);
				} catch (InvalidObjectReference e) {
					logger.log(Level.SEVERE, "A InvalidObjectReference error has occurred. ", e);
				} catch (org.omg.CORBA.ORBPackage.InvalidName e) {
					logger.log(Level.SEVERE, "A org.omg.CORBA.ORBPackage.InvalidName error has occurred. ", e);
				} catch (InvalidName e) {
					logger.log(Level.SEVERE, "A InvalidName error has occurred. ", e);
				} catch (NotFound e) {
					logger.log(Level.SEVERE, "A NotFound error has occurred. ", e);
				} catch (CannotProceed e) {
					logger.log(Level.SEVERE, "A CannotProceed error has occurred. ", e);
				}
			}
		}
		
       try {
               Object objRef = orb.resolve_initial_references("NameService");
               NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
               String name = domainName+"/"+label();
               name = name.replaceAll("\\.", "\\\\.");
               NameComponent[] deviceMgrName = ncRef.to_name(name);
               deviceManagerTie._poa().deactivate_object(deviceManagerTie._poa().reference_to_id(corbaDeviceManager));
               domainManager.unregisterDeviceManager(corbaDeviceManager);
               ncRef.unbind(deviceMgrName);
       } catch (ObjectNotActive e) {
               logger.log(Level.SEVERE, "ObjectNotActive while trying to detach IORs from ORB",e);
       } catch (WrongPolicy e) {
               logger.log(Level.SEVERE, "WrongPolicy while trying to detach IORs from ORB",e);
       } catch (WrongAdapter e) {
               logger.log(Level.SEVERE, "WrongAdapter while trying to detach IORs from ORB",e);
       } catch (InvalidName e) {
               logger.log(Level.SEVERE, "InvalidName while trying to unbind device manager",e);
       } catch (NotFound e) {
               logger.log(Level.SEVERE, "NotFound while trying to unbind device manager",e);
       } catch (CannotProceed e) {
               logger.log(Level.SEVERE, "CannotProceed while trying to unbind device manager",e);
       } catch (org.omg.CORBA.ORBPackage.InvalidName e) {
               logger.log(Level.SEVERE, "InvalidName while trying to unbind device manager",e);
       } catch (Exception e){
           logger.log(Level.SEVERE, "Exception occured while trying to detach IORs from ORB",e);
       }     		
		
	}
	
	@Override
	public void registerDevice(Device device) throws InvalidObjectReference {
		try {
			domainManager.registerDevice(device, this);
		} catch (InvalidProfile e) {
			logger.log(Level.SEVERE, "InvalidProfile while trying to unbind device manager",e);
		} catch (DeviceManagerNotRegistered e) {
			logger.log(Level.SEVERE, "DeviceManagerNotRegistered while trying to unbind device manager",e);
		} catch (RegisterError e) {
			logger.log(Level.SEVERE, "RegisterError while trying to unbind device manager",e);
		}
	}
	
	@Override
	public void registerService(Object serviceObject, String serviceName) throws InvalidObjectReference {
		logger.fine("registerService CALLED!!!");
		
		ServiceType type = new ServiceType();
		type.serviceName = serviceName;
		type.serviceObject = serviceObject;
		synchronized (registeredServices) {
			registeredServices.add(type);
		}
		
		try {
			domainManager.registerService(serviceObject, getCorbaDeviceManager(), serviceName);
		} catch (DeviceManagerNotRegistered e) {
			logger.log(Level.SEVERE, "A DeviceManagerNotRegistered error has occurred. ", e);
		} catch (RegisterError e) {
			logger.log(Level.SEVERE, "A RegisterError error has occurred. ", e);
		}
	}

	@Override
	public void unregisterDevice(Device arg0) throws InvalidObjectReference {
		
	}

	@Override
	public void unregisterService(Object serviceObject, String serviceName) throws InvalidObjectReference {
		logger.fine("unregisterService CALLED!!!");
		
		try {
			domainManager.unregisterService(serviceObject, serviceName);
			
            Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String service = domainName+"/"+serviceName;
            service = service.replaceAll("\\.", "\\\\.");
            NameComponent[] serviceNameComponent = ncRef.to_name(service);
            ncRef.unbind(serviceNameComponent);			
			
			
			synchronized (registeredServices) {
				for(ServiceType type : registeredServices){
					if(type.serviceName.equalsIgnoreCase(serviceName)){
						registeredServices.remove(type);
					}
				}
			}
		} catch (UnregisterError e) {
			logger.log(Level.SEVERE, "A UnregisterError error has occurred. ", e);
		} catch (org.omg.CORBA.ORBPackage.InvalidName e) {
			logger.log(Level.SEVERE, "A org.omg.CORBA.ORBPackage.InvalidName error has occurred. ", e);
		} catch (InvalidName e) {
			logger.log(Level.SEVERE, "A InvalidName error has occurred. ", e);
		} catch (NotFound e) {
			logger.log(Level.SEVERE, "A NotFound error has occurred. ", e);
		} catch (CannotProceed e) {
			logger.log(Level.SEVERE, "A CannotProceed error has occurred. ", e);
		}
	}

	public ORB getOrb() {
		return orb;
	}

	public void setOrb(ORB orb) {
		this.orb = orb;
	}

	@Override
	public Request _create_request(Context arg0, String arg1, NVList arg2, NamedValue arg3) {
		return null;
	}

	@Override
	public Request _create_request(Context arg0, String arg1, NVList arg2, NamedValue arg3, ExceptionList arg4, ContextList arg5) {
		return null;
	}

	@Override
	public Object _duplicate() {
		return null;
	}

	@Override
	public org.omg.CORBA.DomainManager[] _get_domain_managers() {
		return null;
	}

	@Override
	public Object _get_interface_def() {
		return null;
	}

	@Override
	public Policy _get_policy(int arg0) {
		return null;
	}

	@Override
	public int _hash(int arg0) {
		return 0;
	}

	@Override
	public boolean _is_a(String arg0) {
		return false;
	}

	@Override
	public boolean _is_equivalent(Object arg0) {
		return false;
	}

	@Override
	public boolean _non_existent() {
		return false;
	}

	@Override
	public void _release() {
		
	}

	@Override
	public Request _request(String arg0) {
		return null;
	}

	@Override
	public Object _set_policy_override(Policy[] arg0, SetOverrideType arg1) {
		return null;
	}

	public DeviceManager getCorbaDeviceManager() {
		return corbaDeviceManager;
	}

	public void setCorbaDeviceManager(DeviceManager corbaDeviceManager) {
		this.corbaDeviceManager = corbaDeviceManager;
	}

	public DeviceManagerPOATie getDeviceManagerTie() {
		return deviceManagerTie;
	}

	public void setDeviceManagerTie(DeviceManagerPOATie deviceManagerTie) {
		this.deviceManagerTie = deviceManagerTie;
	}
	
	/*
	 * Methods no longer necessary
	@Override
	public Policy _get_client_policy(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object _get_component() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InterfaceDef _get_interface() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ORB _get_orb() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Policy[] _get_policy_overrides(int[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String _repository_id() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object _set_policy_overrides(Policy[] arg0, SetOverrideType arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean _validate_connection(PolicyListHolder arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	*End of Methods no longer necessary
	*/

	@Override
	public DomainManager domMgr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initializeProperties(DataType[] initialProperties)
			throws AlreadyInitialized, InvalidConfiguration, PartialConfiguration {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String registerPropertyListener(Object obj, String[] prop_ids, float interval)
			throws UnknownProperties, InvalidObjectReference {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unregisterPropertyListener(String id) throws InvalidIdentifier {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PortInfoType[] getPortSet() {
		// TODO Auto-generated method stub
		return null;
	}




}

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
package redhawk.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.TRANSIENT;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import CF.DomainManager;
import CF.DomainManagerHelper;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.domain.impl.RedhawkDomainManagerImpl;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;

public class RedhawkDriver implements Redhawk {

	private static Logger logger = Logger.getLogger(RedhawkDriver.class.getName());
	private static final String CORBA_NAME_SERVICE = "ORBInitRef.NameService";
	private static final int DEFAULT_NAMESERVICE_PORT = 2809;
	private static final String DEFAULT_HOSTNAME = "localhost";
    
    private String hostName;
    private int port;
    private ORB orb;
    private Properties connectionProperties = new Properties();
    
    
    /*
     * Disable the CORBA connection error messages for 127.0.0.1. This error occurs on every CORBA call to a remote REDHAWK
     * system because the objects contain references to multiple addresses, and this ORB tries them in order.  There are
     * multiple ways to disable the log messages, but the Filter method short-circuits the logging process the soonest.
     * 
     * The error messages are in the form of:
     *   com.sun.corba.se.impl.transport.SocketOrChannelConnectionImpl <init>
     *   WARNING: "IOP00410201: (COMM_FAILURE) Connection failure: socketType: IIOP_CLEAR_TEXT; hostname: 127.0.0.1; port: 52816"
     *   org.omg.CORBA.COMM_FAILURE:   vmcid: SUN  minor code: 201  completed: No
     */
    static {
    	Logger clog = Logger.getLogger("javax.enterprise.resource.corba._DEFAULT_.rpc.transport");
		clog.setFilter(new Filter() { //filter out specific messages
			public boolean isLoggable(LogRecord record) {
				return !"127.0.0.1".equals(record.getParameters()[1]);  //filter out ones with hostname 127.0.0.1
			}
		});
    }
    
    public RedhawkDriver(){
    	this(DEFAULT_HOSTNAME, DEFAULT_NAMESERVICE_PORT);
    }
    
    public RedhawkDriver(String hostname) {
    	this(hostname, DEFAULT_NAMESERVICE_PORT);
    }
    
    public RedhawkDriver(String hostName, int port){
        this.hostName = hostName;
        this.port = port;
        
        //TODO: Add additional constructor for more Jacorb properties. 
        //Jacorb Properties. Should probably add something so that people can provide
        //additional or different jacorb.properties than the defaults
        //See this guide for more properties: http://www.jacorb.org/releases/3.8/ProgrammingGuide.pdf
        //connectionProperties.put("com.sun.CORBA.transport.ORBUseNIOSelectToWait", "false");
    	//connectionProperties.put("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");
    	//connectionProperties.put("org.omg.PortableInterceptor.ORBInitializerClass.standard_init", "org.jacorb.orb.standardInterceptors.IORInterceptorInitializer");
    	//connectionProperties.put("jacorb.config.dir", System.getProperty("jacorb.config.dir",""));
    	//connectionProperties.put("jacorb.retries", 1);   //jacorb.retries: Number of retries if connection cannot directly be established.
        //connectionProperties.put(CORBA_NAME_SERVICE, "corbaname::" + hostName + ":" + port);
        //connectionProperties.put(System.getProperty("com.sun.CORBA.transport.ORBUseNIOSelectWait", "false");
        
        //System.setProperty("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
        //System.getProperty("jacorb.classloaderpolicy", "forname");
        //TODO: Ask John what this does????
        //if(System.getProperty("redbus.base") != null){
        	//ClassLoader policy for Jacorb to use options are tccl/forname
        	//tccl: 'JacORB will use the thread context class loader to resolve classes and resources'
        	//forname: 'JacORB will use the defining class loader'
        //System.setProperty("jacorb.classloaderpolicy", "forname");
        //}
    }
    
    public RedhawkDriver(String hostName, int port, Properties properties){
    	connectionProperties = properties;
    	this.hostName = hostName; 
    	this.port = port;
    }
    
    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RedhawkDriver [hostName=").append(hostName).append(", port=").append(port).append("]");
		return builder.toString();
	}

	private void initializeOrb(){
		if(orb == null){
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
			logger.log(Level.FINE, "Initializing the Object Request Broker");
			String[] args = new String[2];
			//-ORBInitRef
			//NameService=corbaname::127.0.0.1:2809

			args[0] = "-ORBInitRef";
			args[1] = "NameService=corbaname::"+hostName+":"+port;
			logger.info("Connecting with the following args");
			for(String arg : args){
				logger.info("Args: "+arg);
			}
			
			logger.info("Connection with these properties "+connectionProperties);
			orb = (ORB) ORB.init(args, connectionProperties);
	        Thread.currentThread().setContextClassLoader(cl);
		}
    }
	
	public Map<String, RedhawkDomainManager> getDomains() throws CORBAException {
   		try {
   			initializeOrb();
   			List<RedhawkDomainManager> domainManagers = new ArrayList<RedhawkDomainManager>();
   			Object objRef = orb.resolve_initial_references("NameService");
   			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
   			findDomainManagers(domainManagers, ncRef, ncRef, "");
   			return domainManagers.stream().collect(Collectors.toMap(e -> e.getName(), Function.identity()));
   		} catch (InvalidName e1) {
             throw new CORBAException("A CORBA InvalidName exception was thrown.  This is caused when using the orb to resolve initial references to the Name Service.  " +
                 "Use the nameclt list command on your REDHAWK machine to verify that your Name Service is up.", e1);
		}
	}
    
    /**
     * Find all DomainManagers, by checking if everything on the NameServer is a DomainManager.  Failes if ANY object can't connect.. and takes longer
     */
    private void findDomainManagers(List<RedhawkDomainManager> domainManagers, NamingContextExt rootnc,  NamingContextExt nc, String parent) {
   	 	
	     final int batchSize = 1000;
	     BindingListHolder bList = new BindingListHolder();
	     BindingIteratorHolder bIterator = new BindingIteratorHolder();
	     
	     nc.list(batchSize, bList, bIterator);
   	
	     for(int i=0; i < bList.value.length; i++){
	    	 NameComponent[] name = { bList.value[i].binding_name[0] };
	    	 
	    	 if(bList.value[i].binding_type == BindingType.ncontext){
	    		 try {
		    		 NamingContextExt context = NamingContextExtHelper.narrow(nc.resolve_str(name[0].id));
		    		 findDomainManagers(domainManagers, rootnc, context, parent +  (parent.isEmpty() ? "" : "/") + name[0].id);
	    		 } catch(org.omg.CosNaming.NamingContextPackage.InvalidName | NotFound | CannotProceed e){
	    			 logger.log(Level.FINE, "InvalidName in find domain managers", e);
	    		 }
	    	 } else {
	    		 try {
		    		 DomainManager domMgr = DomainManagerHelper.narrow(rootnc.resolve_str(parent +"/"+ name[0].id));
		    		 domainManagers.add(new RedhawkDomainManagerImpl(this, orb.object_to_string(domMgr), name[0].id));
	    		 } catch(org.omg.CosNaming.NamingContextPackage.InvalidName | BAD_PARAM | COMM_FAILURE | NotFound | TRANSIENT | CannotProceed e){
	    			 /*
	    			  * this catches 1) InvalidName if the item doesn't exist
	    			  *  2) BAD_PARAM if the item isn't a DomainManager
	    			  *  3) COMM_FAILURE if the item exists in the nameserver but doesn't respond
	    			  *  4) NotFound ...
	    			  */
	    		 }
	    	 }
	     }    	
    }
    
    
    public RedhawkDomainManager getDomain(String domainName) throws ResourceNotFoundException, CORBAException {
    	try {
    		logger.log(Level.FINE, "Resolivng the Naming Service on " + hostName + " at port " + port);
	    	initializeOrb();
	        Object objRef = orb.resolve_initial_references("NameService");
	        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	        String domainManagerUrl = domainName+"/"+domainName;
	        DomainManager domainManager = DomainManagerHelper.narrow(ncRef.resolve_str(domainManagerUrl));
	        return new RedhawkDomainManagerImpl(this, orb.object_to_string(domainManager), domainName);
        } catch(InvalidName invalidName){
            throw new CORBAException("A CORBA InvalidName exception was thrown.  This is caused when using the orb to resolve initial references to the Name Service.  " +
                "Use the nameclt list command on your REDHAWK machine to verify that your Name Service is up.", invalidName);
        } catch (NotFound notFound) {
            throw new ResourceNotFoundException("A CORBA NotFound exception was thrown.  This is caused when using the orb to resolve initial references to an object.  " +
                "Either your NameService or your Redhawk Domain Name could not be resolved. Use the nameclt list command on your REDHAWK machine " +
                "to verify that your objects are appropriatly registered.", notFound);
        } catch (CannotProceed cannotProceed) {
            throw new CORBAException("A CORBA CannotProceed exception was thrown.", cannotProceed);
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidDomain) {
            throw new CORBAException("A CORBA InvalidName exception was thrown.  This is caused when using the orb to resolve initial references to the Redhawk Domain.  " +
                "Use the nameclt list command on your REDHAWK machine to verify that your Domain is up.", invalidDomain);
        }
    }
	
    public RedhawkApplication getApplication(String applicationLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException {
    	String[] location = applicationLocation.split("/");
    	
    	if(location.length == 2) {
	    	RedhawkDomainManager domain = null;
	    	try {
				domain = getDomain(location[0]);
			} catch (ResourceNotFoundException | CORBAException e) {
				throw e;
			}
	
	    	try {
	    		return domain.getApplicationByName(location[1]);
			} catch (ResourceNotFoundException e) {
				return domain.getApplicationByIdentifier(location[1]);
			}
    	} else {
    		throw new ResourceNotFoundException("Invalid Resource URI Specified. You must specify a valid location to application. Please use location names separated by slashes(/). You specified:" + applicationLocation);
    	}
    }
    
    public RedhawkDeviceManager getDeviceManager(String deviceManagerLocation) throws ResourceNotFoundException, MultipleResourceException, CORBAException {
    	String[] location = deviceManagerLocation.split("/");
		return getDomain(location[0]).getDeviceManagerByName(location[1]);
    }    
    
    public RedhawkComponent getComponent(String componentLocation) throws ResourceNotFoundException, MultipleResourceException, CORBAException {
    	String[] location = componentLocation.split("/");
    	if(location.length == 3) {
			RedhawkApplication app = getApplication(componentLocation.substring(0, componentLocation.lastIndexOf("/")));
			RedhawkComponent component = app.getComponentByName(location[2]);
			return component;
    	} else {
    		throw new ResourceNotFoundException("Invalid Resource URI Specified. You must specify a valid location to a component. Please use location names separated by slashes(/). You specified:" + componentLocation);
    	}
    }
    
    public RedhawkDevice getDevice(String deviceLocation) throws ResourceNotFoundException, MultipleResourceException, CORBAException {
    	String[] location = deviceLocation.split("/");
    	if(location.length == 3) {
			RedhawkDeviceManager devManager = getDeviceManager(deviceLocation.substring(0, deviceLocation.lastIndexOf("/")));
			RedhawkDevice device = devManager.getDeviceByName(location[2]);
			return device;
    	} else {
    		throw new ResourceNotFoundException("Invalid Resource URI Specified. You must specify a valid location to a component. Please use location names separated by slashes(/). You specified:" + deviceLocation);
    	}    	
    }        
    
    public RedhawkPort getPort(String portLocation) throws ResourceNotFoundException, MultipleResourceException, CORBAException {
    	String[] location = portLocation.split("/");
    	if(location.length == 4){
    		
    		try {
	    		RedhawkComponent component = getComponent(portLocation.substring(0, portLocation.lastIndexOf("/")));
	    		return component.getPort(location[3]);
    		} catch(ResourceNotFoundException e) {
    			RedhawkDevice device = getDevice(portLocation.substring(0, portLocation.lastIndexOf("/")));
    			return device.getPort(location[3]);
    		}
    		
    	} else {
    		throw new ResourceNotFoundException("Invalid Resource URI Specified. You must specify a valid location to either a device or component port. Please use location names separated by slashes(/). You specified:" + portLocation);
    	}
    }
    
    @Override
    public void disconnect() {
    	logger.log(Level.FINE, "Shutting down the Object Request Broker");
        if(orb != null){
        	orb.shutdown(true);
        	orb = null;
        }
    }    

    public ORB getOrb(){
    	//TODO: Why are we initializing an Orb when we get it? Hasn't this already 
    	//Been done?
    	initializeOrb();
        return orb;
    }
    
    /* ===========================   ACCESSOR AND MUTATOR METHODS ================================================ */
    
    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

}
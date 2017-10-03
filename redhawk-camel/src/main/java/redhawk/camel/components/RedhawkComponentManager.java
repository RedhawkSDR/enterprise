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
package redhawk.camel.components;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.spi.ComponentResolver;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redhawk.driver.RedhawkDriver;

public class RedhawkComponentManager {
    
    private ConcurrentHashMap<String, ServiceRegistration> redhawkComponents;
    private ConcurrentHashMap<String, RedhawkComponentResolver> redhawkComponentResolvers;
    private BundleContext bundleContext;
    
    private List<ServiceReference> redhawkConnectionList;
    
    private static Logger logger = LoggerFactory.getLogger(RedhawkComponentManager.class);
    
    private static final String CONNECTION_NAME_PROPERTY = "connectionName";
    private static final String SERVICE_PID_PROPERTY = "service.pid";
	private static final String DOMAIN_MANAGER_PROPERTY = "domainManager";

    public RedhawkComponentManager() {
        redhawkComponents = new ConcurrentHashMap<String, ServiceRegistration>();
        redhawkComponentResolvers = new ConcurrentHashMap<String, RedhawkComponentResolver>();
    }
    
    public void bind(ServiceReference ref) {
        logger.trace("detected the binding a new RedhawkIntegration service");
        String servicePid = (String) ref.getProperty(SERVICE_PID_PROPERTY);
        String connectionName = (String) ref.getProperty(CONNECTION_NAME_PROPERTY);
        String domainName = (String) ref.getProperty(DOMAIN_MANAGER_PROPERTY);
        
        
        if(domainName.trim().isEmpty()) {
        	logger.error("Domain Name not supplied in configuration");
        	return;
        }
        
        logger.trace("Creating a new RedhawkComponentResolver for: " +connectionName);
        RedhawkComponentResolver resolver = new RedhawkComponentResolver((RedhawkDriver)bundleContext.getService(ref), domainName);
        
        Dictionary properties = new Hashtable();
        properties.put("component", connectionName);
        
        logger.trace("Registering redhawk component as a service");
        ServiceRegistration registration = bundleContext.registerService(ComponentResolver.class, resolver, properties);
        
        redhawkComponents.put(servicePid, registration);
        redhawkComponentResolvers.put(servicePid, resolver);
    }
    
    public void unbind(ServiceReference ref) {
        logger.trace("detected the destruction of an existing RedhawkIntegration service");
        
        if(ref != null){
            String servicePid = (String) ref.getProperty(SERVICE_PID_PROPERTY);
            ServiceRegistration registration = redhawkComponents.get(servicePid);
            RedhawkComponentResolver resolver = redhawkComponentResolvers.get(servicePid);
            
            if(registration != null){
                logger.trace("Found corresponding redhawk camel component. Unregistering");
                
                try {
                    resolver.destroyComponentResolver();
                } catch (Exception e){
                    logger.error("Problem when trying to destroy the redhawk component resolver", e);
                }
                
                resolver = null;
                registration.unregister();
            }
        }
    }

    public BundleContext getBundleContext() {
        return bundleContext;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public List<ServiceReference> getRedhawkConnectionList() {
        return redhawkConnectionList;
    }

    public void setRedhawkConnectionList(List<ServiceReference> redhawkConnectionList) {
        this.redhawkConnectionList = redhawkConnectionList;
    }
}

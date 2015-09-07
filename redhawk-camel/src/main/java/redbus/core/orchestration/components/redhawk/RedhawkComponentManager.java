package redbus.core.orchestration.components.redhawk;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.spi.ComponentResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import redhawk.driver.RedhawkDriver;

public class RedhawkComponentManager {
    
    private ConcurrentHashMap<String, ServiceRegistration> redhawkComponents;
    private ConcurrentHashMap<String, RedhawkComponentResolver> redhawkComponentResolvers;
    private BundleContext bundleContext;
    
    private List<ServiceReference> redhawkConnectionList;
    
    private static Log logger = LogFactory.getLog(RedhawkComponentManager.class);
    
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

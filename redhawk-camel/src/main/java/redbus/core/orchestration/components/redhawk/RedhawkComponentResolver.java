package redbus.core.orchestration.components.redhawk;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.spi.ComponentResolver;

import redhawk.driver.RedhawkDriver;

public class RedhawkComponentResolver implements ComponentResolver {

    private ConcurrentHashMap<String, RedhawkComponent> registeredComponents;
    
    private RedhawkDriver redhawkDriver;
    private String domainName;
    
    
    public RedhawkComponentResolver(RedhawkDriver redhawkDriver, String domainName) {
        this.redhawkDriver = redhawkDriver;
        this.domainName = domainName;
        this.registeredComponents = new ConcurrentHashMap<String, RedhawkComponent>();
    }
    
    public Component resolveComponent(String compName, CamelContext context) throws Exception {
        RedhawkComponent component = new RedhawkComponent();
        component.setRedhawkDriver(redhawkDriver);
        component.setDomainName(domainName);
        registeredComponents.put(compName, component);
        return component;
    }

    public void destroyComponentResolver() throws Exception{
        for(RedhawkComponent component : registeredComponents.values()){
            for(Endpoint endpoint : component.getCreatedEndpoints()){
                endpoint.stop();
            }
            component.shutdown();
        }
    }

	public RedhawkDriver getRedhawkDriver() {
		return redhawkDriver;
	}

	public void setRedhawkDriver(RedhawkDriver redhawkDriver) {
		this.redhawkDriver = redhawkDriver;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}


}
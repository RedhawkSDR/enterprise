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
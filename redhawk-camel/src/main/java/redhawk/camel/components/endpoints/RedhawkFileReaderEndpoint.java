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
package redhawk.camel.components.endpoints;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Processor;
import org.apache.camel.impl.ProcessorEndpoint;

import redhawk.driver.RedhawkDriver;

@Deprecated
public class RedhawkFileReaderEndpoint extends ProcessorEndpoint {
    private String scaFilePath;
    private RedhawkDriver redhawkDriver;
    
    public RedhawkFileReaderEndpoint() {
        super();
    }
    public RedhawkFileReaderEndpoint(String endpointUri, CamelContext context, Processor processor) {
        super(endpointUri, context, processor);
    }
    public RedhawkFileReaderEndpoint(String endpointUri, Component component, Processor processor) {
        super(endpointUri, component, processor);
    }
    public RedhawkFileReaderEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }
    public String getScaFilePath() {
        return scaFilePath;
    }
    public void setScaFilePath(String scaFilePath) {
        this.scaFilePath = scaFilePath;
    }
	public RedhawkDriver getRedhawkDriver() {
		return redhawkDriver;
	}
	public void setRedhawkDriver(RedhawkDriver redhawkDriver) {
		this.redhawkDriver = redhawkDriver;
	}
    

}

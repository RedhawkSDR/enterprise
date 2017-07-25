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
package redhawk.rest.model;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import redhawk.driver.device.AdminState;
import redhawk.driver.device.OperationalState;
import redhawk.driver.device.UsageState;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Device {

    private String identifier;
    private String label;
    private boolean started;
    private AdminState adminState;
    private UsageState usageState;
    private OperationalState operationState;
    
    @XmlElementWrapper(name="properties")
    @XmlElement(name="property")
    private List<Property> properties;
    
    //@XmlElement(name="configuration")
    //private Properties configuration; 
    
    //public Properties getConfiguration() {
	//	return configuration;
	//}
    
	//public void setConfiguration(Properties configuration) {
	//	this.configuration = configuration;
	//}
	
	public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public boolean isStarted() {
        return started;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	public AdminState getAdminState() {
		return adminState;
	}
	public void setAdminState(AdminState adminState) {
		this.adminState = adminState;
	}
	public UsageState getUsageState() {
		return usageState;
	}
	public void setUsageState(UsageState usageState) {
		this.usageState = usageState;
	}
	public OperationalState getOperationState() {
		return operationState;
	}
	public void setOperationState(OperationalState operationState) {
		this.operationState = operationState;
	}

}

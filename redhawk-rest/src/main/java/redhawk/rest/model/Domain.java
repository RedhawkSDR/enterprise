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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Domain {

    private String identifier;
    private String name;
    
    @XmlElementWrapper(name="devicemanagers")
    @XmlElement(name="devicemanager")
    private List<DeviceManager> deviceManagers;
    
    @XmlElementWrapper(name="applications")
    @XmlElement(name="application")
    private List<Application> applications;
    
    @XmlElementWrapper(name="properties")
    @XmlElement(name="property")
    private List<Property> properties;
    
    @XmlElementWrapper(name="eventchannels")
    @XmlElement(name="eventchannel")
    private List<EventChannel> eventChannels;
    
    @XmlElementWrapper(name="remoteDomains")
    @XmlElement(name="domainName")
    List<String> remoteDomains = new ArrayList<>();
    
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<DeviceManager> getDeviceManagers() {
        return deviceManagers;
    }
    public void setDeviceManagers(List<DeviceManager> deviceManagers) {
        this.deviceManagers = deviceManagers;
    }
	public List<EventChannel> getEventChannels() {
		return eventChannels;
	}
	public void setEventChannels(List<EventChannel> eventChannels) {
		this.eventChannels = eventChannels;
	}
	public List<Application> getApplications() {
		return applications;
	}
	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	public List<String> getRemoteDomains() {
		return remoteDomains;
	}
	public void setRemoteDomains(List<String> remoteDomains) {
		this.remoteDomains = remoteDomains;
	}

    
}

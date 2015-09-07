package redhawk.rest.model;

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
    
    //@XmlElementWrapper(name="properties")
    //@XmlElement(name="property")
    private List<Property> properties;
    
    @XmlElementWrapper(name="eventChannels")
    @XmlElement(name="eventChannel")
    private List<String> eventChannels;
    
    
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
	public List<String> getEventChannels() {
		return eventChannels;
	}
	public void setEventChannels(List<String> eventChannels) {
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

    
}

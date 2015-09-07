package redhawk.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="devicemanager")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceManager {

    private String label;
    private String identifier;
    
    @XmlElementWrapper(name="services")
    @XmlElement(name="service")
    private List<Service> services;
    
    @XmlElementWrapper(name="devices")
    @XmlElement(name="device")
    private List<Device> devices;
    
    //@XmlElementWrapper(name="properties")
    //@XmlElement(name="property")
    private List<Property> properties;

    
    
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public List<Service> getServices() {
        return services;
    }
    public void setServices(List<Service> services) {
        this.services = services;
    }
    public List<Device> getDevices() {
        return devices;
    }
    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
    
}

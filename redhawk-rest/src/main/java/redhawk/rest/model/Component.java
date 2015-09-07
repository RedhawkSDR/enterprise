package redhawk.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.scd.Softwarecomponent;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Component {

    private boolean started;
    
    private String name;
    
    //@XmlElementWrapper(name="properties")
    //@XmlElement(name="property")
    private List<Property> properties;
    
    @XmlElementWrapper(name="ports")
    @XmlElement(name="port")
    private List<Port> ports;
    
    private Softwarecomponent softwarecomponent; 
    
	private Properties configuration; 
    
    public boolean isStarted() {
        return started;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Port> getPorts() {
		return ports;
	}
	public void setPorts(List<Port> ports) {
		this.ports = ports;
	}
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	public Properties getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Properties configuration) {
		this.configuration = configuration;
	}
	
    public Softwarecomponent getSoftwareComponent() {
		return softwarecomponent;
	}
    
	public void setSoftwareComponent(Softwarecomponent softwareComponent) {
		this.softwarecomponent = softwareComponent;
	}
}

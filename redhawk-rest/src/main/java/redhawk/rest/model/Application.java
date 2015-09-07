package redhawk.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Application {

    private String identifier;
    private String name;
    private boolean started;
    
    @XmlElementWrapper(name="components")
    @XmlElement(name="component")
    private List<Component> components;
    
    //@XmlElementWrapper(name="externalPorts")
    //@XmlElement(name="port")
    private List<Port> externalPorts;
    
    //@XmlElementWrapper(name="properties")
    //@XmlElement(name="property")
    private List<Property> properties;
    
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
    public boolean isStarted() {
        return started;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }
    public List<Component> getComponents() {
        return components;
    }
    public void setComponents(List<Component> components) {
        this.components = components;
    }
	public List<Port> getExternalPorts() {
		return externalPorts;
	}
	public void setExternalPorts(List<Port> externalPorts) {
		this.externalPorts = externalPorts;
	}
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
    

}

package redhawk.rest.model;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Device {

    private String identifier;
    private String label;
    private boolean started;
    
    //@XmlElementWrapper(name="properties")
    //@XmlElement(name="property")
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

}

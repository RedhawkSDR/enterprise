package redhawk.rest.model;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="properties")
@XmlAccessorType(XmlAccessType.FIELD)
public class PropertyContainer {

	public PropertyContainer() {
	}
	
	public PropertyContainer(List<Property> properties){
		this.properties = properties;
	}
	
	
	@XmlElementWrapper(name="properties")
    @XmlElement(name="property")
    private List<Property> properties;

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
    
}
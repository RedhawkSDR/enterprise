package redhawk.rest.model;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="components")
@XmlAccessorType(XmlAccessType.FIELD)
public class ComponentContainer {

    @XmlElement(name="component")
    private List<Component> components;

    public ComponentContainer(){    	
    }
    
    public ComponentContainer(List<Component> components){
    	this.components = components;
    }
    
	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

    
}
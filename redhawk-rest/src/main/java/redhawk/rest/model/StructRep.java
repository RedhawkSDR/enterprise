package redhawk.rest.model;

import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import redhawk.driver.xml.model.sca.prf.ConfigurationKind;



@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StructRep extends Property {

    private static Logger logger = Logger.getLogger(StructRep.class.getName());

    protected List<ConfigurationKind> kinds;
    protected List<Property> attributes;
    
    
	public List<ConfigurationKind> getKinds() {
		return kinds;
	}
	public void setKinds(List<ConfigurationKind> kinds) {
		this.kinds = kinds;
	}
	public List<Property> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Property> attributes) {
		this.attributes = attributes;
	}
    
    
    
    
    
}


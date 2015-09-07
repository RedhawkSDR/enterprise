package redhawk.rest.model;

import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import redhawk.driver.xml.model.sca.prf.ConfigurationKind;



@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StructSequenceRep extends Property {

    private static Logger logger = Logger.getLogger(StructSequenceRep.class.getName());

    protected List<ConfigurationKind> configurationKinds;
    protected List<Property> structs;
    
    
	public List<Property> getStructs() {
		return structs;
	}
	public void setStructs(List<Property> structs) {
		this.structs = structs;
	}
	public List<ConfigurationKind> getConfigurationKinds() {
		return configurationKinds;
	}
	public void setConfigurationKinds(List<ConfigurationKind> configurationKinds) {
		this.configurationKinds = configurationKinds;
	}
    
    
    
    
    
}


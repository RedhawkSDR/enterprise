package redhawk.rest.model;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ports")
@XmlAccessorType(XmlAccessType.FIELD)
public class PortContainer {

    @XmlElement(name="port")
    private List<Port> ports;

    public PortContainer(){    	
    }
    
    public PortContainer(List<Port> ports){
    	this.ports = ports;
    }
    
	public List<Port> getPorts() {
		return ports;
	}

	public void setPorts(List<Port> ports) {
		this.ports = ports;
	}

    
}
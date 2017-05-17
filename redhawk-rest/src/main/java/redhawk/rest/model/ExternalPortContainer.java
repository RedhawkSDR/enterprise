package redhawk.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="externalports")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExternalPortContainer {
    @XmlElement(name="port")
    private List<ExternalPort> ports;

	public ExternalPortContainer(){
		
	}
	
	public ExternalPortContainer(List<ExternalPort> ports) {
		super();
		this.ports = ports;
	}
    
	public List<ExternalPort> getPorts() {
		return ports;
	}

	public void setPorts(List<ExternalPort> ports) {
		this.ports = ports;
	}
	
}

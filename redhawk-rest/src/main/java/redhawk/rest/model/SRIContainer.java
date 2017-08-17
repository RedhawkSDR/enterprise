package redhawk.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import redhawk.driver.port.RedhawkStreamSRI;

@XmlRootElement(name="sris")
@XmlAccessorType(XmlAccessType.FIELD)
public class SRIContainer {
	@XmlElementWrapper(name="sris")
	@XmlElement(name="sri")
	private List<RedhawkStreamSRI> sris;
	
	public SRIContainer(){}
	
	public SRIContainer(List<RedhawkStreamSRI> sris){
		this.setSris(sris);
	}

	public List<RedhawkStreamSRI> getSris() {
		return sris;
	}

	public void setSris(List<RedhawkStreamSRI> sris) {
		this.sris = sris;
	}
}

package redhawk.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WaveformInfo {

	public String id;
	public String name;
	public String sadLocation;
	  
	public String getSadLocation() {
		return sadLocation;
	}
	public void setSadLocation(String sadLocation) {
		this.sadLocation = sadLocation;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
  
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

  
  
}
package redhawk.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EventChannel {
	private String name;
	
    @XmlElementWrapper(name="registrationIds")
    @XmlElement(name="registrationId")	
    List<String> registrantIds;

	public List<String> getRegistrantIds() {
		return registrantIds;
	}

	public void setRegistrantIds(List<String> registrantIds) {
		this.registrantIds = registrantIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String eventChannelName) {
		this.name = eventChannelName;
	}
}

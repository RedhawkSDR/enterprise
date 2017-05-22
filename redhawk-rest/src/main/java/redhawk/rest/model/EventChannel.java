package redhawk.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EventChannel {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String eventChannelName) {
		this.name = eventChannelName;
	}
}

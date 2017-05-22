package redhawk.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="eventchannels")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventChannelContainer {
	@XmlElement(name="eventchannel")
	private List<EventChannel> eventChannels; 
	
	public EventChannelContainer(){}
	
	public EventChannelContainer(List<EventChannel> channels){
		this.eventChannels = channels;
	}

	public List<EventChannel> getEventChannels() {
		return eventChannels;
	}

	public void setEventChannels(List<EventChannel> eventChannels) {
		this.eventChannels = eventChannels;
	}

	
}

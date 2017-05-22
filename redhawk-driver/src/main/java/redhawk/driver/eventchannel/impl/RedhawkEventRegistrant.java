package redhawk.driver.eventchannel.impl;

import CF.EventChannelManagerPackage.EventRegistrant;
import CF.EventChannelManagerPackage.EventRegistration;

public class RedhawkEventRegistrant {
	private String registrationId;
	
	private String channelName;
	
	private EventRegistrant registrant;
	
	public RedhawkEventRegistrant(String id, String channelName, EventRegistrant r){
		this.registrationId = id;
		this.channelName = channelName;
		this.registrant = r;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public EventRegistrant getRegistrant() {
		return registrant;
	}

	public void setRegistrant(EventRegistrant registrant) {
		this.registrant = registrant;
	}
	
	public EventRegistration getEventRegistration(){
		return new EventRegistration(this.registrationId, this.channelName);
	}
	
}

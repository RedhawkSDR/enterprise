package redhawk.driver.connectionmanager.impl;

import redhawk.driver.eventchannel.RedhawkEventChannel;

public class RedhawkEventChannelEndpoint extends RedhawkEndpoint{
	private RedhawkEventChannel channel; 
	
	public RedhawkEventChannelEndpoint(EndpointType type, String resourceId, RedhawkEventChannel eventChannel) {
		super(type, resourceId);
		this.channel = eventChannel;
	}

	public RedhawkEventChannel getChannel() {
		return channel;
	}

	public void setChannel(RedhawkEventChannel channel) {
		this.channel = channel;
	}

}

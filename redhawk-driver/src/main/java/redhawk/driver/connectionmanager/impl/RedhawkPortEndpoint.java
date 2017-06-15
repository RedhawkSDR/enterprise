package redhawk.driver.connectionmanager.impl;

import redhawk.driver.port.RedhawkPort;

public class RedhawkPortEndpoint extends RedhawkEndpoint{
	private RedhawkPort port; 
	
	public RedhawkPortEndpoint(EndpointType type, String resourceId, RedhawkPort port) {
		super(type, resourceId);
		this.port = port;
	}

	public RedhawkPort getPort() {
		return port;
	}

	public void setPort(RedhawkPort port) {
		this.port = port;
	}

}

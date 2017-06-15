package redhawk.driver.connectionmanager.impl;

import redhawk.driver.port.RedhawkPort;

public class RedhawkEndpoint {

	private EndpointType type;
	
	private RedhawkPort port;
	
	private String resourceId;
		
	public RedhawkEndpoint(EndpointType type, String resourceId, RedhawkPort port){
		this.type = type;
		this.port = port;
		this.resourceId = resourceId;
	}

	public EndpointType getType() {
		return type;
	}

	public void setType(EndpointType type) {
		this.type = type;
	}

	public RedhawkPort getPort() {
		return port;
	}

	public void setPort(RedhawkPort port) {
		this.port = port;
	}
	
	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
}

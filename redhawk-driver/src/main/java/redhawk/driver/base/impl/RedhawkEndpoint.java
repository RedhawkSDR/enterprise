package redhawk.driver.base.impl;

import redhawk.driver.port.RedhawkPort;

public class RedhawkEndpoint {
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	private EndpointType type;
	
	private RedhawkPort port;
	
	private String uniqueId;
		
	public RedhawkEndpoint(EndpointType type, String uniqueId, RedhawkPort port){
		this.type = type;
		this.port = port;
		this.uniqueId = uniqueId;
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
	
	
}

package redhawk.driver.connectionmanager.impl;

import redhawk.driver.port.RedhawkPort;

public class ConnectionInfo {
	private RedhawkPort providesPort;
	
	private RedhawkPort usesPort; 
	
	private String connectionId;
	
	private String requestorId;
	
	private String connectionRecordId; 
	
	private Boolean connected;
	
	public ConnectionInfo(){}

	public RedhawkPort getProvidesPort() {
		return providesPort;
	}

	public void setProvidesPort(RedhawkPort providesPort) {
		this.providesPort = providesPort;
	}

	public RedhawkPort getUsesPort() {
		return usesPort;
	}

	public void setUsesPort(RedhawkPort usesPort) {
		this.usesPort = usesPort;
	}

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

	public String getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(String requestorId) {
		this.requestorId = requestorId;
	}

	public String getConnectionRecordId() {
		return connectionRecordId;
	}

	public void setConnectionRecordId(String connectionRecordId) {
		this.connectionRecordId = connectionRecordId;
	}

	public Boolean getConnected() {
		return connected;
	}

	public void setConnected(Boolean connected) {
		this.connected = connected;
	} 
	
	
}

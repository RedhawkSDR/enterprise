package redhawk.driver.connectionmanager.impl;

import redhawk.driver.base.impl.RedhawkEndpoint;

public class ConnectionInfo {
	private RedhawkEndpoint providesEndpoint;
	
	private RedhawkEndpoint usesEndpoint; 
	
	private String connectionId;
	
	private String requestorId;
	
	private String connectionRecordId; 
	
	private Boolean connected;
	
	public ConnectionInfo(){}

	public RedhawkEndpoint getProvidesPort() {
		return providesEndpoint;
	}

	public void setProvidesPort(RedhawkEndpoint providesPort) {
		this.providesEndpoint = providesPort;
	}

	public RedhawkEndpoint getUsesPort() {
		return usesEndpoint;
	}

	public void setUsesPort(RedhawkEndpoint usesPort) {
		this.usesEndpoint = usesPort;
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

	@Override
	public String toString() {
		return "ConnectionInfo [providesPort=" + providesEndpoint + ", usesPort=" + usesEndpoint + ", connectionId="
				+ connectionId + ", requestorId=" + requestorId + ", connectionRecordId=" + connectionRecordId
				+ ", connected=" + connected + "]";
	} 
	
	
}

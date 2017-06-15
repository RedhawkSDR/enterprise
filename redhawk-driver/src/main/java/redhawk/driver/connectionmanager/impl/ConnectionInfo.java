package redhawk.driver.connectionmanager.impl;

public class ConnectionInfo {
	private RedhawkEndpointStatus providesEndpoint;
	
	private RedhawkEndpointStatus usesEndpoint; 
	
	private String connectionId;
	
	private String requestorId;
	
	private String connectionRecordId; 
	
	private Boolean connected;
	
	public ConnectionInfo(){}

	public RedhawkEndpointStatus getProvidesEndpointStatus() {
		return providesEndpoint;
	}

	public void setProvidesEndpointStatus(RedhawkEndpointStatus providesEndpointStatus) {
		this.providesEndpoint = providesEndpointStatus;
	}

	public RedhawkEndpointStatus getUsesEndpointStatus() {
		return usesEndpoint;
	}

	public void setUsesEndpointStatus(RedhawkEndpointStatus usesEndpointStatus) {
		this.usesEndpoint = usesEndpointStatus;
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
		return "ConnectionInfo [providesEndpoint=" + providesEndpoint + ", usesEndpoint=" + usesEndpoint
				+ ", connectionId=" + connectionId + ", requestorId=" + requestorId + ", connectionRecordId="
				+ connectionRecordId + ", connected=" + connected + "]";
	}

		
}

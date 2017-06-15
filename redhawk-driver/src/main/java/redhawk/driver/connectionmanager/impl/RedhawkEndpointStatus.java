package redhawk.driver.connectionmanager.impl;

import org.omg.CORBA.Object;

public class RedhawkEndpointStatus {
	private String portName;
	
	private String repositoryId;
	
	private String entityId;
	
	private org.omg.CORBA.Object endpointObject;

	public RedhawkEndpointStatus(String portName, String repositoryId, String entityId, Object endpointObject) {
		super();
		this.portName = portName;
		this.repositoryId = repositoryId;
		this.entityId = entityId;
		this.endpointObject = endpointObject;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public String getRepositoryId() {
		return repositoryId;
	}

	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public org.omg.CORBA.Object getEndpointObject() {
		return endpointObject;
	}

	public void setEndpointObject(org.omg.CORBA.Object endpointObject) {
		this.endpointObject = endpointObject;
	}
	
	@Override
	public String toString() {
		return "RedhawkEndpointStatus [portName=" + portName + ", repositoryId=" + repositoryId + ", entityId="
				+ entityId + ", endpointObject=" + endpointObject + "]";
	}
}

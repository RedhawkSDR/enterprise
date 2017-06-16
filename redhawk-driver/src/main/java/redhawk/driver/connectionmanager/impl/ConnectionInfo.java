/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
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

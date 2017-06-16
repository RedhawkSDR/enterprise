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

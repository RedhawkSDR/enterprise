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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CF.ConnectionManager;
import CF.ConnectionManagerHelper;
import CF.ConnectionStatusIteratorHolder;
import CF.ConnectionManagerPackage.ConnectionStatusSequenceHolder;
import CF.ConnectionManagerPackage.ConnectionStatusType;
import CF.ConnectionManagerPackage.EndpointKind;
import CF.ConnectionManagerPackage.EndpointRequest;
import CF.ConnectionManagerPackage.EndpointResolutionType;
import CF.PortPackage.InvalidPort;
import redhawk.driver.base.impl.CorbaBackedObject;
import redhawk.driver.base.impl.EndpointType;
import redhawk.driver.base.impl.RedhawkEndpoint;
import redhawk.driver.connectionmanager.RedhawkConnectionManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;

public class RedhawkConnectionManagerImpl extends CorbaBackedObject<ConnectionManager> implements RedhawkConnectionManager {

	private ConnectionManager connectionManager;
	
	public RedhawkConnectionManagerImpl(RedhawkDomainManager mgr, ConnectionManager connectionManager) {
		super(mgr.getDriver().getOrb().object_to_string(connectionManager), mgr.getDriver().getOrb());
		this.connectionManager = connectionManager;
	}

	@Override
	public void connect(RedhawkEndpoint usesPort, RedhawkEndpoint providesPort, String requestId, String connectionId) throws ConnectionException{
		//TODO: Move this to a method
		try {
			connectionManager.connect(this.getEndpointRequestFromRedhawkEndpoint(usesPort), 
					this.getEndpointRequestFromRedhawkEndpoint(providesPort), requestId, connectionId);
		} catch (InvalidPort | IOException e) {
			e.printStackTrace();
			throw new ConnectionException("Issue connecting these two endpoints [provides: "+usesPort+" uses: "+providesPort+"] "+e.getMessage(), e);
		}
	}

	@Override
	public void disconnect(String connectionRecordId) {
		try {
			connectionManager.disconnect(connectionRecordId);
		} catch (InvalidPort e) {
			throw new ConnectionException("Unable do disconnect using this id: "+connectionRecordId);
		}
	}

	@Override
	public List<ConnectionInfo> getConnections() {
		List<ConnectionInfo> connections = new ArrayList<>();
		
		for(ConnectionStatusType connection : connectionManager.connections()){
			ConnectionInfo info = new ConnectionInfo(); 
			info.setConnected(connection.connected);
			info.setConnectionId(connection.connectionId);
			info.setConnectionRecordId(connection.connectionRecordId);
			info.setConnected(connection.connected);
			info.setRequestorId(connection.requesterId);
						
			connections.add(info);
		}
		
		return connections;
	}

	@Override
	protected ConnectionManager locateCorbaObject() throws ResourceNotFoundException {
		return ConnectionManagerHelper.narrow(getOrb().string_to_object(this.getIor()));
	}

	@Override
	public Class<?> getHelperClass() {
		// TODO Auto-generated method stub
		return ConnectionManagerHelper.class;
	}

	@Override
	public ConnectionManager getCorbaObj() {
		// TODO Auto-generated method stub
		return this.getCorbaObject();
	}
	
	private EndpointRequest getEndpointRequestFromRedhawkEndpoint(RedhawkEndpoint point) throws IOException{
		EndpointResolutionType request = new EndpointResolutionType();
		switch(point.getType()){
		case Application:
			request.applicationId(point.getUniqueId());
			//request.objectRef(point.getPort().getCorbaObject());
			return new EndpointRequest(request, "tunerFloat_in");
		case Device:
			request.deviceId(point.getUniqueId());
			//request.objectRef(point.getPort().getCorbaObject());
			return new EndpointRequest(request, point.getPort().getName());
		default:
			throw new IOException("Unhandled Endpoint Type kind");
		}
	}
}

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

import java.util.ArrayList;
import java.util.List;

import CF.ConnectionManager;
import CF.ConnectionManagerHelper;
import CF.ConnectionStatusIteratorHolder;
import CF.ConnectionManagerPackage.ConnectionStatusSequenceHolder;
import CF.ConnectionManagerPackage.ConnectionStatusType;
import redhawk.driver.base.impl.CorbaBackedObject;
import redhawk.driver.connectionmanager.RedhawkConnectionManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;

public class RedhawkConnectionManagerImpl extends CorbaBackedObject<ConnectionManager> implements RedhawkConnectionManager {

	private ConnectionManager connectionManager;
	
	public RedhawkConnectionManagerImpl(RedhawkDomainManager mgr, ConnectionManager connectionManager) {
		super(mgr.getDriver().getOrb().object_to_string(connectionManager), mgr.getDriver().getOrb());
		this.connectionManager = connectionManager;
	}

	@Override
	public void connect(RedhawkPort outputPort, RedhawkPort inputPort, String requestId, String connectionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect(String connectionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ConnectionInfo> getConnections() {
		List<ConnectionInfo> connections = new ArrayList<>();
		ConnectionStatusSequenceHolder holder = new ConnectionStatusSequenceHolder(); 
		ConnectionStatusIteratorHolder iter = new ConnectionStatusIteratorHolder();
		
		connectionManager.listConnections(100000, holder, iter);

		for(ConnectionStatusType connection : holder.value){
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
}

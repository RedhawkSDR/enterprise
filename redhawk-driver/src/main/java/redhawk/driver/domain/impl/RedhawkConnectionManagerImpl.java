package redhawk.driver.domain.impl;

import CF.ConnectionManager;
import redhawk.driver.domain.RedhawkConnectionManager;

public class RedhawkConnectionManagerImpl implements RedhawkConnectionManager {

	private ConnectionManager connectionManager;
	
	public RedhawkConnectionManagerImpl(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}
}

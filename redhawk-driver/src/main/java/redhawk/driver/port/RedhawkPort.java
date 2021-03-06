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
package redhawk.driver.port;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import BULKIO.ProvidesPortStatisticsProvider;
import BULKIO.ProvidesPortStatisticsProviderHelper;
import BULKIO.StreamSRI;
import BULKIO.updateSRI;
import BULKIO.updateSRIHelper;
import CF.PortPackage.InvalidPort;
import CF.PortPackage.OccupiedPort;
import ExtendedCF.QueryablePort;
import ExtendedCF.QueryablePortHelper;
import ExtendedCF.UsesConnection;
import redhawk.driver.bulkio.Packet;
import redhawk.driver.exceptions.PortException;

public interface RedhawkPort {
	/**
	 * Uses port type constant {@value #PORT_TYPE_USES}
	 */
	public static final String PORT_TYPE_USES = "uses";

	/**
	 * Provides port type constant {@value #PORT_TYPE_PROVIDES}
	 */
	public static final String PORT_TYPE_PROVIDES = "provides";

	/**
	 * Bidirectional port type constant {@value #PORT_TYPE_BIDIRECTIONAL}
	 */
	public static final String PORT_TYPE_BIDIRECTIONAL = "bidirectional";

	/**
	 * Put logic for what to do with data coming out of a port in your listener.
	 * 
	 * @param portListener
	 *            Object containing logic for what to do when you get data on a
	 *            port.
	 * @deprecated use more appropriately name listen method.
	 * 
	 * @throws Exception
	 */
	@Deprecated
	public void connect(PortListener<?> portListener) throws Exception;
	
	default void connect(RedhawkPort port) throws PortException {
		connect(port, "rhdriver_" + UUID.randomUUID());
	}
	
	default void connect(RedhawkPort port, String connectionId) throws PortException {
		// Initial check for type difference
		if (this.getType().equals(port.getType()) || !this.getRepId().equals(port.getRepId()))
			throw new PortException("Cannot connect ports of the same type or w/ non matching interfaces(repId).");
		
		try {
			CF.Port aPort = null;
			if (this.getType().equals(RedhawkPort.PORT_TYPE_USES)) {
				aPort = CF.PortHelper.narrow(this.getCorbaObject());

				aPort.connectPort(port.getCorbaObject(), connectionId);
			} else {
				aPort = CF.PortHelper.narrow(port.getCorbaObject());
				aPort.connectPort(this.getCorbaObject(), connectionId);
			}
		} catch (InvalidPort | OccupiedPort e) {
			throw new PortException("Unable to connect ports", e);
		}
	}
	

	
	public void listen(PortListener<?> portListener) throws Exception;
	
	/**
	 * Disconnect from a port.
	 * 
	 * @throws PortException
	 */
	public void disconnect() throws PortException;

	/**
	 * Disconnect a specific connection from the port.
	 * 
	 * @param connectionId
	 */
	public void disconnect(String connectionId) throws PortException;

	/**
	 * Send data to a port.
	 * 
	 * @param packet
	 * @throws Exception
	 */
	public <T> void send(Packet<T> packet) throws Exception;

	/**
	 * @return List of port statistics objects.
	 */
	public List<RedhawkPortStatistics> getPortStatistics();

	/**
	 * @return Representation Id for a Redhawk Port.
	 * 
	 */
	public String getRepId();

	/**
	 * @return port type.
	 */
	public String getType();

	/**
	 * @return port name.
	 */
	public String getName();

	/**
	 * 
	 * @return Active SRIs
	 */
	default List<RedhawkStreamSRI> getActiveSRIs() throws PortException {
		String portType = this.getType();
		List<RedhawkStreamSRI> rhSRI = new ArrayList<>();

		if (portType.equalsIgnoreCase("provides")) {
			try {
				updateSRI sris = updateSRIHelper.narrow(this.getCorbaObject());				
			
				// Do this with Java 8 stuff at some point
				for (StreamSRI sri : sris.activeSRIs())
					rhSRI.add(new RedhawkStreamSRI(sri));

				return rhSRI;
			}catch(org.omg.CORBA.BAD_PARAM ex) {
				throw new PortException("Unable to narrow port to "+updateSRI.class.getName(), ex);
			}
		} else {
			throw new PortException("Uses Port does not support activeSRIs method");
		}
	}

	/**
	 * @return CORBA object representing port.
	 */
	public org.omg.CORBA.Object getCorbaObject();

	/**
	 * Returns the State of the Port. This enum maps to BULKIO.PortUsageType
	 * 
	 * @return
	 * @throws UnsupportedOperationException
	 *             Occurs when you call this method on a Uses Port.
	 */
	default PortState getPortState() throws PortException {
		String portType = this.getType();

		// Only Provides ports have state
		if (portType.equalsIgnoreCase("provides")) {
			try {
				ProvidesPortStatisticsProvider provider = ProvidesPortStatisticsProviderHelper
						.narrow(this.getCorbaObject());
				return PortState.reverseLookup(provider.state().value());
			}catch(org.omg.CORBA.BAD_PARAM ex) {
				throw new PortException("Unable to narrow port to "+ProvidesPortStatisticsProvider.class.getName(), ex);
			}
		} else {
			throw new PortException("Uses Port do not support state method.");
		}
	}

	/**
	 * Returns the connectionIds for a uses port
	 * 
	 * @return
	 */
	default List<String> getConnectionIds() throws PortException {
		String portType = this.getType();
		List<String> connectionIds = new ArrayList<>();

		// Only Uses ports have connections
		if (portType.equalsIgnoreCase("uses")) {
			try {
				QueryablePort qPort = QueryablePortHelper.narrow(this.getCorbaObject());
				
				if(qPort==null)
					throw new PortException("Null CORBAObject returned for port.");
				
				for (UsesConnection connection : qPort.connections()) {
					connectionIds.add(connection.connectionId);
				}

				return connectionIds;
			} catch (org.omg.CORBA.BAD_PARAM ex) {
				throw new PortException("Unable to narrow port to "+QueryablePort.class.getName(), ex);
			}
		} else {
			throw new PortException("Provides Port does not support connections() method.");
		}
	}
	// TODO: Would be helpful to get dataType
}

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

import java.util.List;

import redhawk.driver.bulkio.Packet;
import redhawk.driver.exceptions.PortException;
import CF.PortPackage.InvalidPort;

public interface RedhawkPort {

	public static final String PORT_TYPE_USES = "uses";
	public static final String PORT_TYPE_PROVIDES = "provides";
	public static final String PORT_TYPE_BIDIRECTIONAL = "bidirectional";
	
    public void connect(PortListener<?> portListener) throws Exception;
    public void disconnect() throws InvalidPort, PortException;
    public <T> void send(Packet<T> packet) throws Exception;
    
    public List<RedhawkPortStatistics> getPortStatistics();
    public String getRepId();
    public String getType();
	public String getName();
	
	public org.omg.CORBA.Object getCorbaObject();

}

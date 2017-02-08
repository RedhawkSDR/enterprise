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
package redhawk.websocket.jsr356;

import java.util.concurrent.ConcurrentHashMap;

import redhawk.driver.Redhawk;
import redhawk.websocket.jsr356.processor.WebSocketProcessor;
import redhawk.websocket.jsr356.sockets.RedhawkBulkIoWebSocket;

public class WebSocketDriverManager {

	public static ConcurrentHashMap<String, Redhawk> redhawkDrivers = new ConcurrentHashMap<>();
	
	public static ConcurrentHashMap<String, RedhawkBulkIoWebSocket> portConnections = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, WebSocketProcessor> webSocketProcessors = new ConcurrentHashMap<>();
	
	
}

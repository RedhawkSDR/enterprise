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
package redhawk;

import javax.websocket.DeploymentException;

import org.junit.Ignore;

@Ignore("This is also not in proper unit test setup...")
public class WebSocketTest {

	public static void main(String[] args) throws DeploymentException {

//        Server server = new Server("localhost", 8025, "/redhawk/websocket", EventChannelEndpoint.class, DevicePortEndpoint.class, ComponentPortEndpoint.class, ShortHandComponentPortEndpoint.class);
//        while(true) {
//        	server.start();
//        }

		Object o = null;
		
		Long s = (Long) o;
		
		if(s == null) {
			System.out.println("YUP");
		}
		
		System.out.println(s);
		
	}
	
}

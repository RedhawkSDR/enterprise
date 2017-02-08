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
package redhawk.websocket.sockets;

import org.eclipse.jetty.websocket.api.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.event.EventAdmin;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
public class RedhawkEventAdminWebSocketTest {
	@Test
	public void letsGetCoverageForCoverageSakes() throws InvalidSyntaxException{
		EventAdmin admin = Mockito.mock(EventAdmin.class);
		Session session  = Mockito.mock(Session.class);
		//PowerMockito.mockStatic(OsgiUtils.class);
		//Mockito.when(OsgiUtils.getService(EventAdmin.class)).thenReturn(admin);
	
		RedhawkEventAdminWebSocket socket = new RedhawkEventAdminWebSocket(false, null, null);
		socket.onWebSocketConnect(session);
		socket.onWebSocketClose(1, "Hello Is It Me Your Looking For.");
	}
}

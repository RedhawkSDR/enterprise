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
package redhawk.websocket;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.ServiceReference;

import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;

@Ignore("Once again u need to clean this up it used to work just fine...")
public class RedhawkWebSocketServletTest {
	@Test
	public void testBindAndUnbindRedhawkDrivers(){
		RedhawkWebSocketServlet servlet = new RedhawkWebSocketServlet();
		
		//Should I have to do this? 
		servlet.setRedhawkDriverServices(new ArrayList<>());
		ServiceReference<Redhawk> rh = Mockito.mock(ServiceReference.class);
		
//		Mockito.when(rh.getAlias()).thenReturn("anAlias");
		
		servlet.bindRedhawk(rh);
		assertEquals("Should now be one RH services.", 1, servlet.getRedhawkDrivers().size());
		
		servlet.unbindRedhawk(rh);
		assertEquals("Should now be zero RH drivers.", 0, servlet.getRedhawkDrivers().size());
	}
	
	@Test
	public void testBindAndUnbindWebsocketProcessors(){
		RedhawkWebSocketServlet servlet = new RedhawkWebSocketServlet();
		
		//Should I have to do this? 
		servlet.setRedhawkDriverServices(new ArrayList<>());
		WebSocketProcessor ws = Mockito.mock(WebSocketProcessor.class);
		
		Mockito.when(ws.getName()).thenReturn("aName");
		
		servlet.bindProcessor(ws);
		assertEquals("Should now be one Websocket Processor.", 1, servlet.getWebSocketProcessors().size());
		
		servlet.unbindProcessor(ws);
		assertEquals("Should now be zero Websocket drivers.", 0, servlet.getWebSocketProcessors().size());
	}
}

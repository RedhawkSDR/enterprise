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
package redhawk.websocket.eventchannel.listeners;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.omg.CORBA.Any;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TypeCodePackage.BadKind;

//TODO: This test may no longer be relevant...
public class ObjectEventChannelListenersTest {
	@Test
	public void test() throws BadKind{
		Any any = Mockito.mock(Any.class);
		TypeCode code = Mockito.mock(TypeCode.class);
		
		Mockito.when(any.type()).thenReturn(code);
		Mockito.when(code.id()).thenThrow(BadKind.class);
		SampleListener listener = new SampleListener(); 
		
		assertEquals(any, listener.processMessage(any));
	}
	
	class SampleListener extends ObjectEventChannelListener{

		@Override
		public void onMessage(Object message) {
		}
	}
}

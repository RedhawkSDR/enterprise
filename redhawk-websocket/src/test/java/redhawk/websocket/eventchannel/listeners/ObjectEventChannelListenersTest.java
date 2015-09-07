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

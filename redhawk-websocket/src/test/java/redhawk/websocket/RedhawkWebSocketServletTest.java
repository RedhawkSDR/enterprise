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

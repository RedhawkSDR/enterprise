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

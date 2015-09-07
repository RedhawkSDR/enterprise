package redhawk.websocket.sockets;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import redhawk.driver.Redhawk;

/**
 * This class interacts with the EventAdmin when new Websockets are created and closed. 
 */
public class RedhawkEventAdminWebSocket extends WebSocketAdapter {

    private static Logger logger = Logger.getLogger(RedhawkEventAdminWebSocket.class.getName());
    private Redhawk redhawk;
    private boolean newDriverInstance;
    private String path;

	public RedhawkEventAdminWebSocket(boolean newDriverInstance, Redhawk redhawk, String path) {
        this.newDriverInstance = newDriverInstance;
        this.redhawk = redhawk;
        this.path = path;
    }

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);

        /*
        /* TODO: Pretty sure this is never hit since both the classes that extend
         * the Admin override these methods. ...
         * EventAdmin eventAdmin;
        try {
            eventAdmin = OsgiUtils.getService(EventAdmin.class);
            if (eventAdmin != null) {
                Map<String, String> pathMap = new HashMap<>();
                pathMap.put("path", this.path);
                eventAdmin.postEvent(new Event("REDHAWKWEBSOCKET/open", pathMap));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error posting oepn event: ", e);
        }
        */
    }

    @Override
    public void onWebSocketClose(int closeCode, String message) {
        super.onWebSocketClose(closeCode, message);

        // if running in an OSGi container, post the event to EventAdmin
        /* TODO: Pretty sure this is never hit since both the classes that extend
         * the Admin override these methods. 
         * EventAdmin eventAdmin;
        try {
            eventAdmin = OsgiUtils.getService(EventAdmin.class);
            if (eventAdmin != null) {
                Map<String, String> pathMap = new HashMap<>();
                pathMap.put("path", this.path);
                eventAdmin.postEvent(new Event("REDHAWKWEBSOCKET/close", pathMap));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error posting close event: ", e);
        }
        */
    }

    public boolean isNewDriverInstance() {
        return newDriverInstance;
    }

    public void setNewDriverInstance(boolean newDriverInstance) {
        this.newDriverInstance = newDriverInstance;
    }

    public Redhawk getRedhawk() {
        return redhawk;
    }

    public void setRedhawk(Redhawk redhawk) {
        this.redhawk = redhawk;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

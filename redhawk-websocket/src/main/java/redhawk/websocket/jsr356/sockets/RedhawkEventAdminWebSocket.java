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
package redhawk.websocket.jsr356.sockets;

import java.util.logging.Logger;

import javax.websocket.Session;

import redhawk.driver.Redhawk;

/**
 * This class interacts with the EventAdmin when new Websockets are created and closed. 
 */
public class RedhawkEventAdminWebSocket {

    private static final Logger logger = Logger.getLogger(RedhawkEventAdminWebSocket.class.getName());
    private Redhawk redhawk;
    private boolean newDriverInstance;
    private String path;

    public RedhawkEventAdminWebSocket(boolean newDriverInstance, Redhawk redhawk, String path) {
        this.newDriverInstance = newDriverInstance;
        this.redhawk = redhawk;
        this.path = path;
    }

    public void onWebSocketConnect(Session session) {

//        // if running in an OSGi container, post the event to EventAdmin
//        EventAdmin eventAdmin;
//        try {
//            eventAdmin = OsgiUtils.getService(EventAdmin.class);
//            if (eventAdmin != null) {
//                Map<String, String> pathMap = new HashMap<>();
//                pathMap.put("path", this.path);
//                eventAdmin.postEvent(new Event("REDHAWKWEBSOCKET/open", pathMap));
//            }
//        } catch (Exception e) {
//            logger.error("Error posting oepn event: ", e);
//        }
    }

    public void onWebSocketClose(int closeCode, String message) {

//        // if running in an OSGi container, post the event to EventAdmin
//        EventAdmin eventAdmin;
//        try {
//            eventAdmin = OsgiUtils.getService(EventAdmin.class);
//            if (eventAdmin != null) {
//                Map<String, String> pathMap = new HashMap<>();
//                pathMap.put("path", this.path);
//                eventAdmin.postEvent(new Event("REDHAWKWEBSOCKET/close", pathMap));
//            }
//        } catch (Exception e) {
//            logger.error("Error posting close event: ", e);
//        }
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

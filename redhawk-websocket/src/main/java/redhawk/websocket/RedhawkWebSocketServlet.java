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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import redhawk.driver.Redhawk;

/**
 * Servlet to configure Websockets and WebsocketProcessors
 *
 */
public class RedhawkWebSocketServlet extends WebSocketServlet {

	private static final long serialVersionUID = 492698370478866728L;
	private static Logger logger = Logger.getLogger(RedhawkWebSocketServlet.class.getName());
	private BundleContext context;

	private List<ServiceReference<Redhawk>> redhawkDriverServices;
	private Map<String, Redhawk> redhawkDrivers = new ConcurrentHashMap<String, Redhawk>();
	
	private List<WebSocketProcessor> webSocketProcessorServices;
	private Map<String, WebSocketProcessor> webSocketProcessors = new ConcurrentHashMap<String, WebSocketProcessor>();
	
	// nameserver/domains/domainId/devicemanagers/devicemanager/devices/device/ports/port
	// nameserver/domains/domainId/applications/application/components/component/ports/port
	// nameserver/domains/domainId/eventchannels/eventChannelName
	@Override
	public void configure(WebSocketServletFactory webSocketServletFactory) {
		logger.info("Creating WebSocket...");
		webSocketServletFactory.setCreator(new RedhawkWebSocketCreator(redhawkDriverServices, webSocketProcessorServices, webSocketProcessors, redhawkDrivers));
	}

	public List<ServiceReference<Redhawk>> getRedhawkDriverServices() {
		return redhawkDriverServices;
	}

	public void setRedhawkDriverServices(List<ServiceReference<Redhawk>> redhawkDriverServices) {
		this.redhawkDriverServices = redhawkDriverServices;
	}

	public List<WebSocketProcessor> getWebSocketProcessorServices() {
		return webSocketProcessorServices;
	}

	public Map<String, WebSocketProcessor> getWebSocketProcessors() {
		return webSocketProcessors;
	}

	public Map<String, Redhawk> getRedhawkDrivers() {
		return redhawkDrivers;
	}

	public void setWebSocketProcessorServices(List<WebSocketProcessor> webSocketProcessorServices) {
		this.webSocketProcessorServices = webSocketProcessorServices;
	}

	public void bindProcessor(WebSocketProcessor processor) {
		webSocketProcessors.put(processor.getName().toLowerCase(), processor);
	}

	public void unbindProcessor(WebSocketProcessor processor) {
		if(processor!=null & webSocketProcessors!=null){
			webSocketProcessors.remove(processor.getName().toLowerCase());
		}
	}

	public void bindRedhawk(ServiceReference<Redhawk> reference) {
		logger.info("References is: "+reference);
		logger.info("Context is "+context);
		String connectionName = (String) reference.getProperty("connectionName");
		
		if(context!=null){
			if (connectionName != null) {
				redhawkDrivers.put(connectionName, context.getService(reference));
			}			
		}else{
			logger.log(Level.SEVERE, "UMMMMM WHY is context null");
		}
	}

	public void unbindRedhawk(ServiceReference<Redhawk> reference) {
        if(reference!=null && reference.getProperty("connectionName")!=null){
    		String connectionName = (String) reference.getProperty("connectionName");

    		if (connectionName != null) {
    			redhawkDrivers.remove(connectionName);
    		}
        }else{
        	logger.log(Level.FINE, "Unable to unbind Null reference passed in or no connectionName present");
        }
	}

	public BundleContext getContext() {
		logger.info("Called getContext()");
		return context;
	}

	public void setContext(BundleContext context) {
		logger.info("Called setContext()");
		this.context = context;
	}
}

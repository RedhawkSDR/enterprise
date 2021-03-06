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
package redhawk.websocket.test.util;

import java.util.Scanner;

import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import redhawk.websocket.RedhawkWebSocketServlet;

/**
 * Support class for launching a JettyServer that hosts the REDHAWKWebSocket.  
 *
 */
public class JettySupport { 
	public static void main(String args[]) throws Exception{
		Server server = setupJettyServer();
		Scanner scanner = new Scanner(System.in);
	    System.out.println("Enter exit to quit:");
	    Boolean dontStopBelieving = true;
	    while(dontStopBelieving){
	    	String input = scanner.next();
	    	if(input.equals("exit")){
	    		dontStopBelieving=false;
	    	}else{
	    		System.out.println("Entered: "+input);
	    	}
	    }
        
        System.out.println("Finished");
        System.exit(0);
	}
	
	public static Server setupJettyServer(){
        //Source taken from EventServer class here: https://github.com/jetty-project/embedded-jetty-websocket-examples/tree/master/native-jetty-websocket-example/src/main/java/org/eclipse/jetty/demo
		Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        Integer port = Integer.valueOf(System.getProperty("jetty.port", "8781"));
        System.out.println("PORT is: "+port);
        connector.setPort(port);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        
        // Add a websocket to a specific path spec
        ServletHolder holderEvents = new ServletHolder("ws-events", RedhawkWebSocketServlet.class);
        context.addServlet(holderEvents, "/redhawk/*");

        try
        {
            server.start();
            server.dump(System.err);
            //server.join();
        }
        catch (Throwable t)
        {
        	t.printStackTrace();
        	System.err.println("FAILED!!!!!!!!!!!!!!!");
        }
        
        return server;
	}
}

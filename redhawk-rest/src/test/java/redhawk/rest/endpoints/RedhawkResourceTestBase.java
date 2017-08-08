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
package redhawk.rest.endpoints;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import redhawk.testutils.RedhawkTestBase;

public class RedhawkResourceTestBase extends RedhawkTestBase{
	static Server server;
	
	static Client client; 
	
	public static String baseURI;
	
	public static String domainName = "REDHAWK_DEV";
		
	@BeforeClass
	public static void setup() throws Exception{
		baseURI = "http://localhost:8080/redhawk/"+domainHost+":"+domainPort+"/domains";
		server = new Server(8080);
		WebAppContext webapp = new WebAppContext();
		webapp.setResourceBase("src/test/resources/webapp");
		server.setHandler(webapp);
		System.out.println("Starting embedded Jetty");
		server.start();

		client = ClientBuilder.newBuilder().newClient();
		
		//Set up base uri
		logger.info("Domain name: "+domainName+" Host: "+domainHost+" Port: "+domainPort+" baseURI: "+baseURI);
	}
	
	@AfterClass
	public static void tearDown() throws Exception{
		System.out.println("Stopping embedded Jetty");
		server.stop();
	}
}


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
package redhawk.websocket.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redhawk.driver.application.RedhawkApplication;
import redhawk.websocket.test.util.RedhawkTestUtil;
import redhawk.websocket.test.util.RedhawkWebSocketTestUtil;

@Ignore
public class WebsocketProcessorIT {
    private static Logger logger = LoggerFactory.getLogger(WebsocketProcessorIT.class);

	private static RedhawkApplication application;
		
	private final File webPage = new File(
			"src/test/resources/webapp/wstest.html");
	
	static String KARAF_HOME = System.getenv().get("KARAF_HOME");;
	
	private static Boolean headless;
	
	private static Server server;
	
	@BeforeClass
	public static void setup() throws Exception{
		assertTrue("REDHAWK_DEV must exist to run this System test. Pass in param -DskipSystemTests=true to skip this test.", RedhawkTestUtil.redhawkDevExists());
		//TODO: This could be done with paxexam....
		assertTrue("KARAF must be running to run this System test because it deploys a sample Processor bundle for management. Pass in param -DskipSystemTests=true to skip this test.",
				RedhawkTestUtil.karafIsRunning());
		application = RedhawkTestUtil.launchApplication(false); 
		
		//Connects to running REDBUS because it needs to hit find Processors
		deployProcessor();
		
		//TODO: Do something cleaner then pausing so the processor can be deployed. 
		Thread.sleep(5000l);
	}
	
	@Test
	public void testProcessorLifecycleManagement() throws Exception{
		System.setProperty("jetty.port", "8181");
		String endpoint; 
		WebSocketClient client = new WebSocketClient();
		endpoint = RedhawkTestUtil.sampleWebSocketPortEndpoint("dataFloat_out.json");

		System.out.println("Endpoint: "+endpoint);
		RedhawkWebSocketTestUtil socket = new RedhawkWebSocketTestUtil(5);
		try {
			try {
				client.start();

				// Attempt Connect
				Future<Session> fut = client.connect(socket, new URI(endpoint));
                Session session = fut.get();
                //List the available processors 
                session.getRemote().sendString("listAvailableProcessors");
                
                //Add a processor
                session.getRemote().sendString("processors:[{\"processorName\":\"myprocessordecimator\", \"processorConfiguration\":{}}]");
                
                //Get Processors
                session.getRemote().sendString("getProcessors");
				
                //Remove processor
                session.getRemote().sendString("removeProcessor:myprocessordecimator");
                
                //Make sure it's gone
                session.getRemote().sendString("getProcessors");

                //Add processor again
                session.getRemote().sendString("processors:[{\"processorName\":\"myprocessordecimator\", \"processorConfiguration\":{}}]");

                //Make sure it's gone
                session.getRemote().sendString("getProcessors");
                
                //Clear processor
                session.getRemote().sendString("clearProcessors");
                
                //Make sure it's gone
                session.getRemote().sendString("getProcessors");
                
                while (socket.getMessageCount() < socket.getMessagesToKeep()) {
					logger.info("hello world");
				}
			} finally {
				client.stop();
			}
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}	
		
		System.out.println(socket.getData());
		String[] data = socket.getData().split("\n");
		
		assertTrue(
				"Socket data should indicate that it was able to connect to web server",
				socket.getData().contains("socket connected: "));
		assertTrue(
				"Socket data should indicate that it was able to disconnect from web server",
				socket.getData().contains("socket disconnected: "));
		
		assertEquals("received text message: [\"myprocessordecimator\"]", data[1].trim());		
		
		assertEquals("received text message: [{\"processorName\":\"myprocessordecimator\",\"processorConfiguration\":{}}]", data[2].trim());

		assertEquals("received text message: []", data[3].trim());
		
		assertEquals("received text message: [{\"processorName\":\"myprocessordecimator\",\"processorConfiguration\":{}}]", data[4].trim());

		assertEquals("received text message: []", data[5].trim());
	}
	
	@AfterClass
	public static void cleanup() throws Exception {
		//Need to go back to default once this test is done. 
		System.setProperty("jetty.port", "8781");
		if(application!=null)
			application.release();
		
		removeProcessor();
		
		// Clear the
		//if(driver!=null)
		//	driver.close();
	}
	
	public static void clear(){
		//driver.findElement(By.id("clear_component_ports")).click();
	}
	
	public static void deployProcessor() throws IOException{
		FileUtils.copyFileToDirectory(new File("src/test/resources/processor/processor-example-1.0.0.RELEASE-SNAPSHOT.jar"), new File(KARAF_HOME+"/deploy"));
	}
	
	public static void removeProcessor() throws IOException{
		File blah = new File(KARAF_HOME+"/deploy/processor-example-1.0.0.RELEASE-SNAPSHOT.jar");
		blah.delete();
	}
}

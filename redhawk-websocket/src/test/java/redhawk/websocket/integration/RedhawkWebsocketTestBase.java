package redhawk.websocket.integration;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationStartException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.testutils.RedhawkTestBase;
import redhawk.websocket.RedhawkWebSocketServlet;
import redhawk.websocket.test.util.JettySupport;

public class RedhawkWebsocketTestBase extends RedhawkTestBase{
	static Server server; 
	
	static RedhawkApplication application;
	
	@BeforeClass
	public static void setupJetty() throws ApplicationStartException{
		File waveForm = new File("src/test/resources/waveform/wf-integration-test.sad.xml");
		
		try {
			application = driver.getDomain().createApplication("WebsocketTest", waveForm);
			application.start();
		} catch (MultipleResourceException | ApplicationCreationException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail("Unable to launch waveform that test depends on "+e.getMessage());
		}
		
		//Start jetty server
		server = JettySupport.setupJettyServer();
	}
	
	private void setupJettyServlet(){
		//TODO: Clean up ports
		server = new Server(8080);
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		
		//Set up path
		context.setContextPath("/");
		context.addServlet(new ServletHolder(new RedhawkWebSocketServlet()), "/redhawk/*");
		
		server.setHandler(context);
	}
	
	@AfterClass
	public static void tearDownJetty() throws Exception{
		if(application!=null)
			application.release();
		
		//Delete waveform from File System
		driver.getDomain().getFileManager().removeDirectory("/waveforms/wf-integration-test");
		server.stop();
	}
}

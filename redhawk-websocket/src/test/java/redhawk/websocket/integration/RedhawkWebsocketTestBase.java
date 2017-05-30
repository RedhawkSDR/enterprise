package redhawk.websocket.integration;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.ApplicationStartException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.testutils.RedhawkTestBase;
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
		}
		
		//Start jetty server
		server = JettySupport.setupJettyServer();
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

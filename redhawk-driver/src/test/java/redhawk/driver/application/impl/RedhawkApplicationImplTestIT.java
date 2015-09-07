package redhawk.driver.application.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.ApplicationStartException;
import redhawk.driver.exceptions.ApplicationStopException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkApplicationImplTestIT {
	private RedhawkDriver driver; 
	
	private String applicationName = "myTestApplication"; 
	
	private RedhawkApplication application; 
	
	@Before
	public void setup() throws ResourceNotFoundException, ApplicationCreationException, CORBAException, MultipleResourceException{
		//Create Application
		driver = new RedhawkDriver();
		driver.getDomain("REDHAWK_DEV").createApplication(applicationName, new File("src/test/resources/waveforms/rh/testWaveform.sad.xml"));
		application = driver.getApplication("REDHAWK_DEV/"+applicationName);
		assertNotNull(application);
	}
	
	
	@Test
	public void testApplicationLifeCycleManagement() throws ApplicationStartException, ApplicationStopException{
		assertEquals(applicationName, application.getName());
		application.start();
		assertEquals("Application should be started", true, application.isStarted());
		application.stop();
		assertEquals("Application should be stopped", false, application.isStarted());
	}
	
	@Test
	public void testGetAssembly() throws IOException{
		assertNotNull(application.getAssembly());	
	}
	
	@Test
	public void testGetComponents() throws MultipleResourceException, ResourceNotFoundException{
		List<RedhawkComponent> redhawkComponents = application.getComponents();
		assertEquals("There should be two components in the test waveform", 2, redhawkComponents.size());
		
		//Make sure you can retrieve each component by name 
		for(RedhawkComponent component : redhawkComponents){
			assertNotNull(application.getComponentByName(component.getName()));
		}	
	}
	
	//TODO: Add test for getting externalports
	
	@After
	public void shutdown() throws ApplicationReleaseException, ConnectionException, ResourceNotFoundException, IOException, CORBAException{
		//Release application and clean it up from $SDRROOT
		application.release();
		driver.getDomain("REDHAWK_DEV").getFileManager().removeDirectory("/waveforms/testWaveform");
	}
}

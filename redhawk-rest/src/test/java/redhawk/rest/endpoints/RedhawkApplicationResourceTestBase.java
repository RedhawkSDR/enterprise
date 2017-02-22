package redhawk.rest.endpoints;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkApplicationResourceTestBase extends RedhawkResourceTestBase{
	static RedhawkDriver driver;
	
	static RedhawkDomainManager domain; 
	
	static String applicationName = "MyApplication";
	
	@BeforeClass
	public static void setupApplication() throws ResourceNotFoundException, ApplicationCreationException, CORBAException{
		driver = new RedhawkDriver();
		domain = driver.getDomain("REDHAWK_DEV");
		
		domain.createApplication(applicationName, "/waveforms/rh/FM_mono_demo/FM_mono_demo.sad.xml");
	}
	
	@AfterClass
	public static void tearDownApplication() throws ApplicationReleaseException{
		for(RedhawkApplication application : domain.getApplications()){
			application.release();
		}
	}
}

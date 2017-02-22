package redhawk.rest.endpoints;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkApplicationResourceIT extends RedhawkResourceTestBase{
	private static RedhawkDriver driver;
	
	private static RedhawkDomainManager domain; 
	
	private static String applicationName = "MyApplication";
	
	@BeforeClass
	public static void setupApplication() throws ResourceNotFoundException, ApplicationCreationException, CORBAException{
		driver = new RedhawkDriver();
		domain = driver.getDomain("REDHAWK_DEV");
		
		domain.createApplication(applicationName, "/waveforms/rh/FM_mono_demo/FM_mono_demo.sad.xml");
	}
	
	@Test
	public void testGetApplications() throws InterruptedException{
		WebTarget target = client.target(baseUri+"/localhost:2809/domains/"+domainName+"/applications");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testGetApplication(){
		WebTarget target = client.target(baseUri+"/localhost:2809/domains/"+domainName+"/applications/"+applicationName);
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testGetApplicationProperties(){
		WebTarget target = client.target(baseUri+"/localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/properties");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());
	}
	
	@AfterClass
	public static void tearDownApplication() throws ApplicationReleaseException{
		for(RedhawkApplication application : domain.getApplications()){
			application.release();
		}
	}
}

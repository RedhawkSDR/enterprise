package redhawk.rest.endpoints;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
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
import redhawk.rest.model.WaveformInfo;

public class RedhawkApplicationResourceIT extends RedhawkApplicationResourceTestBase{
	@Test
	public void testGetApplications() throws InterruptedException{
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testGetApplication(){
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName);
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testGetApplicationProperties(){
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName+"/properties");
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testLaunchAndReleaseApplication(){
		String applicationName = "restLaunchedApp";
		WebClient client = WebClient.create(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName);
		
		WaveformInfo info = new WaveformInfo();
		info.setSadLocation("/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
		info.setId(UUID.randomUUID().toString());
		info.setName(applicationName);
		
		client.type(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML);
		Response r = client.put(info);
		assertEquals(200, r.getStatus());
		
		r = client.delete();
		assertEquals(200, r.getStatus());
	}
	
	@Test
	public void testLaunchAndReleaseApplicationJSON(){
		String applicationName = "restLaunchedApp";
		WebClient client = WebClient.create(baseUri+"localhost:2809/domains/"+domainName+"/applications/"+applicationName);
		
		WaveformInfo info = new WaveformInfo();
		info.setSadLocation("/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
		info.setId(UUID.randomUUID().toString());
		info.setName(applicationName);
		
		client.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		Response r = client.put(info);
		assertEquals(200, r.getStatus());
		
		r = client.delete();
		assertEquals(200, r.getStatus());
	}
}

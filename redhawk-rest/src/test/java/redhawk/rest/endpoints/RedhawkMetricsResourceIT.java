package redhawk.rest.endpoints;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;

@Ignore("Figure out how to make JSON work on unit tests")
public class RedhawkMetricsResourceIT extends RedhawkResourceTestBase{
	static String applicationName = "MyApplication";

	@BeforeClass
	public static void launchApplication(){
		try {
			driver.getDomain().createApplication(applicationName, "/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
		} catch (ApplicationCreationException | MultipleResourceException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetMetrics() throws InterruptedException{
		WebTarget target = client.target(baseURI+"/"+domainName+"/metrics");
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		assertEquals(200, response.getStatus());
	}
}

package redhawk.rest.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.rest.RedhawkManager;
import redhawk.rest.model.ApplicationMetrics;
import redhawk.rest.model.PortMetrics;
import redhawk.rest.model.RedhawkMetrics;
import redhawk.rest.utils.MetricTypes;
import redhawk.testutils.RedhawkTestBase;

public class MetricConverterIT extends RedhawkTestBase{
	private static RedhawkManager manager = new RedhawkManager(); 
	
	private static RedhawkApplication application;
	
	private static String appName = "myApp";

	@BeforeClass
	public static void setup(){
		//Launch app 
		try {
			application = driver.getDomain().createApplication(appName, "/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
		} catch (MultipleResourceException | ApplicationCreationException | CORBAException e) {
			e.printStackTrace();
			fail("Test is not setup properly unable to launch application "+e.getMessage());
		}
	}
	
	@Test
	public void testGetMetrics() {
		RedhawkMetrics metrics = MetricsConverter.getMetrics(manager, nameServer, domainName);
		
		assertNotNull(metrics.getApplicationMetrics());
		assertNotNull(metrics.getPortStatistics());
	}
	
	@Test
	public void testGetAppMetrics() {
		List<ApplicationMetrics> metrics = MetricsConverter.getMetricByType(manager, nameServer, domainName, MetricTypes.APPLICATION);
		
		assertTrue("Metrics should not be empty", !metrics.isEmpty());
	}
	
	@Test
	public void testGetPortMetrics() {
		List<PortMetrics> metrics = MetricsConverter.getMetricByType(manager, nameServer, domainName, MetricTypes.PORT);
		
		assertTrue("Metrics should not be empty", !metrics.isEmpty());
	}
	
	@AfterClass
	public static void cleanup() throws ApplicationReleaseException {
		if(application!=null)
			application.release();
	}
}

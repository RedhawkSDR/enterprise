package redhawk.rest.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import CF.DataType;
import CF.PropertiesHolder;
import CF.PropertySetHelper;
import CF.PropertySetOperations;
import CF.UnknownProperties;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.properties.RedhawkProperty;
import redhawk.driver.properties.RedhawkStruct;
import redhawk.driver.properties.RedhawkStructSequence;
import redhawk.rest.RedhawkManager;
import redhawk.rest.model.ApplicationMetrics;
import redhawk.rest.model.GPPMetrics;
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
	
	@Test
	public void testGetGPPMetrics() {
		List<GPPMetrics> metrics = MetricsConverter.getMetricByType(manager, nameServer, domainName, MetricTypes.GPP);
	
		assertTrue("Metrics should not be empty", !metrics.isEmpty());
	}
	
	@Test
	public void testHelloWorld() throws MultipleResourceException, CORBAException {
		RedhawkDevice device =  driver.getDevice();
		Object property = device.getProperty("DCE:cdc5ee18-7ceb-4ae6-bf4c-31f983179b4d");
		
		//Native
		System.out.println("Returned: "+property);
		
		//Raw!!!!
    	PropertySetOperations properties = PropertySetHelper.narrow(device.getCorbaObj());
        PropertiesHolder ph = new PropertiesHolder();
    	//String[] propNames = new String[] {"device_kind"};
    	String[] propNames = new String[] {"utilization"};
        ph.value = this.stringToDTArray(propNames);
    	
    	try {
			properties.query(ph);
		} catch (UnknownProperties e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private DataType[] stringToDTArray(String[] props) {
        List<DataType> dataTypes = new ArrayList<DataType>();
        for(String propertyName : props){
            dataTypes.add(new DataType(propertyName, driver.getOrb().create_any()));
        }
        
       
        return dataTypes.toArray(new DataType[dataTypes.size()]);
	}
	
	
	@AfterClass
	public static void cleanup() throws ApplicationReleaseException {
		if(application!=null)
			application.release();
	}
}

package redhawk.camel.components.endpoints;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.base.RedhawkFileSystem;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.ApplicationStartException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;

public class RedhawkDataEndpointTestIT extends CamelTestSupport { 
	private static final String waveformName = "myDemo";
	
	private final String componentName = "DataConverter_1.*";
	
	private final String floatPortName = "dataFloat_out";
	
	private final String doublePortName = "dataDouble_out";
	
	private final String floatPortType = "float";
	
	private final String doublePortType = "double";
	
	private final String baseUri = "redhawk://data:localhost:2809:REDHAWK_DEV?waveformName="+waveformName+"&componentName="+componentName+"&portName=";
	
	private final String dataFloatUri = baseUri+floatPortName+"&portType="+floatPortType;
	
	private final String dataDoubleUri = baseUri+doublePortName+"&portType="+doublePortType;
	
	private final String dataShortUri = baseUri+"dataShort_out&portType=short";

	private final String dataOctetUri = baseUri+"dataOctet_out&portType=octet";
	
	@EndpointInject(uri = "mock:floatResult")
    protected MockEndpoint floatResultEndpoint;
	
	@EndpointInject(uri = "mock:doubleResult")
    protected MockEndpoint doubleResultEndpoint;
	
	@EndpointInject(uri = "mock:octetResult")
    protected MockEndpoint octetResultEndpoint;
	
	@EndpointInject(uri ="mock:shortResult")
    protected MockEndpoint shortResultEndpoint;	

	@EndpointInject(uri = dataFloatUri)
	protected RedhawkDataEndpoint floatDataEndpoint;
	
	@EndpointInject(uri = dataDoubleUri)
	protected RedhawkDataEndpoint doubleDataEndpoint;
	
	@EndpointInject(uri = dataShortUri)
	protected RedhawkDataEndpoint shortDataEndpoint;
	
	@EndpointInject(uri = dataOctetUri)
	protected RedhawkDataEndpoint octetDataEndpoint;
	
	private static RedhawkDriver driver; 

	private static RedhawkApplication rhApplication;
	
	private static RedhawkFileSystem rhFS;

	@BeforeClass
	public static void setup() throws ConnectionException, MultipleResourceException, CORBAException, FileNotFoundException, IOException, ApplicationCreationException, ApplicationStartException{
		driver = new RedhawkDriver();
		
		rhFS = driver.getDomain().getFileManager();

		//Deploy application
		rhApplication = driver.getDomain().createApplication(waveformName, new File("src/test/resources/waveforms/demoWaveform/demoWaveform.sad.xml"));
		rhApplication.start();
	}
	
	@Test
	public void testPortToCamel() throws InterruptedException{
		/*
		 * Expecting a minimum number of messages for each redhawk endpoint 
		 * to satisfy that it's receiving data 
		 */
		floatResultEndpoint.expectedMinimumMessageCount(10);
		doubleResultEndpoint.expectedMinimumMessageCount(10);
		octetResultEndpoint.expectedMinimumMessageCount(10);
		shortResultEndpoint.expectedMinimumMessageCount(10);
		
		Thread.sleep(5000l);
		
		/*
		 * Ensuring that assert has been satisfied. 
		 */
		floatResultEndpoint.assertIsSatisfied();
		doubleResultEndpoint.assertIsSatisfied();
		octetResultEndpoint.assertIsSatisfied();
		shortResultEndpoint.assertIsSatisfied();
	}
	
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                /*
                 * Route to ensure can receive port data point to point
                 */
            	from(floatDataEndpoint)
                .log("received float data")
                .to(floatResultEndpoint);
            	
            	/*
            	 * Route to ensure receiving double data works  
            	 */
            	from(doubleDataEndpoint)
                .log("received double data")
                .to(doubleResultEndpoint);

                /*
                 * Route to ensure receiving short data works
                 */
            	from(shortDataEndpoint)
                .log("received short data")
                .to(shortResultEndpoint);
            	
                /*
                 * Route to ensure receiving octet data works
                 */
            	from(octetDataEndpoint)
                .log("received octet data")
                .to(octetResultEndpoint);
            }
        };
    }
    
	@AfterClass
	public static void cleanup() throws IOException, ApplicationReleaseException{
		if(rhApplication!=null)
			rhApplication.release();
		
		rhFS.removeDirectory("/waveforms/demoWaveform");
		
		if(driver!=null){
			driver.disconnect();
		}
	}
}

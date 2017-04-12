package redhawk.camel.components.endpoints;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class RedhawkDataEndpointTestIT extends CamelTestSupport{
	private final String waveformName = "myDemo";
	
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
}

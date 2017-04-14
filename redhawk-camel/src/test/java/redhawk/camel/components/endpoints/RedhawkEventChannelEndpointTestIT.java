package redhawk.camel.components.endpoints;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
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
import redhawk.testutils.RedhawkTestUtils;

public class RedhawkEventChannelEndpointTestIT extends CamelTestSupport{
    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;
    
    @EndpointInject(uri = "mock:producerResult")
    protected MockEndpoint pResultEndpoint;
    
    @EndpointInject(uri = "redhawk://event-channel:localhost:2809:REDHAWK_DEV?eventChannelName=EventsSpat&dataTypeName=messages")
    protected RedhawkEventChannelEndpoint eventChannelConsumerEndpoint;
    
    @EndpointInject(uri ="redhawk://event-channel:localhost:2809:REDHAWK_DEV?eventChannelName=testChannel&messageId=fooBar")
    protected RedhawkEventChannelEndpoint eventChannelProducerEndpoint;
    
    @Produce(uri = "direct:start")
    protected ProducerTemplate template;
    
	private static RedhawkDriver driver; 
	
	private static RedhawkFileSystem rhFS;
	
	private static RedhawkApplication rhApplication;
	
	@BeforeClass
	public static void setup() throws ConnectionException, MultipleResourceException, CORBAException, FileNotFoundException, IOException, ApplicationCreationException, ApplicationStartException{
		driver = new RedhawkDriver();
		
		rhFS = driver.getDomain().getFileManager();

		/*
		 * Deploy EventSpitter Component 
		 */
		RedhawkTestUtils.writeJavaComponentToCF("../demo/camel-event-channel/src/main/resources/EventSpitter", rhFS);
	
		
		//Deploy application
		rhApplication = driver.getDomain().createApplication("spitToChannel", new File("../demo/camel-event-channel/src/main/resources/SpitToChannel/SpitToChannel.sad.xml"));
		rhApplication.start();
	}
    
    /*
     * Waveform sends out data every 10 seconds 
     * so make sure you have a minimum number of messages 
     * reflecting that you're able to use the component to subscribe to
     * messages on an event channel. 
     */
    @Test
    public void testEventChannelEndpoint() throws InterruptedException{
    	resultEndpoint.expectedMinimumMessageCount(5);
    	Thread.sleep(10000l);
    	resultEndpoint.assertIsSatisfied();
    }
    
    /*
     * Test sending data directly to an event channel
     */
    @Test
    public void testEventProducerChannel() throws InterruptedException{
    	Map<String, Object> bo = new HashMap<>();
    	bo.put("id", "testId");
    	bo.put("value", "smdflsmdflsmdflmdsflmsdflmsdlfm");
    	template.sendBody(bo);
    	
    	pResultEndpoint.expectedMessageCount(1);
    	
    	Thread.sleep(5000l);
    	
    	pResultEndpoint.assertIsSatisfied();
    }
    
    
    
    //TODO: Add tests for sending data to an EventChannel 
    
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from(eventChannelConsumerEndpoint)
                .to("log:eventTest")
                .to(resultEndpoint);
                
                from("direct:start")
                .to(eventChannelProducerEndpoint)
                .to(pResultEndpoint);
            }
        };
    }
    
	@AfterClass
	public static void cleanup() throws IOException, ApplicationReleaseException{
		if(rhApplication!=null)
			rhApplication.release();
		
		rhFS.removeDirectory("/waveforms/SpitToChannel");
		rhFS.removeDirectory("/components/EventSpitter");
		
		if(driver!=null){
			driver.disconnect();
		}
	}
}

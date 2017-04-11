package redhawk.camel.components.endpoints;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class RedhawkEventChannelEndpointTestIT extends CamelTestSupport{
    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;
    
    @EndpointInject(uri = "redhawk://event-channel:localhost:2809:REDHAWK_DEV?eventChannelName=EventsSpat&dataTypeName=messages")
    protected RedhawkEventChannelEndpoint eventChannelEndpoint;
    
    /*@Override
    protected CamelContext createCamelContext() throws Exception {
    	CamelContext context = new DefaultCamelContext();

		RedhawkComponent component = new RedhawkComponent();
		//context.addComponent("redhawk", component);
		
		return context;
    }*/
    
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
    
    //TODO: Add tests for sending data to an EventChannel 
    
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from(eventChannelEndpoint)
                .to("log:eventTest")
                .to(resultEndpoint);
            }
        };
    }
}

package redhawk.camel.components.endpoints;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

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
}

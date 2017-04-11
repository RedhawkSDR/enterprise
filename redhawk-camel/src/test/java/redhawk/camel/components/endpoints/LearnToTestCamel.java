package redhawk.camel.components.endpoints;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Before;
import org.junit.Test;

import redhawk.camel.components.RedhawkComponent;
import redhawk.camel.components.endpoints.RedhawkEventChannelEndpoint;

public class LearnToTestCamel { 
	private CamelContext context = new DefaultCamelContext(); 
	
	@EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;
    
	RedhawkEventChannelEndpoint rhEventChannelEndpoint;
	
	@Before
	public void setup() throws Exception{
		RedhawkComponent component = new RedhawkComponent(); 
		MockComponent mockComponent = new MockComponent();
		Map<String, Object> params = new HashMap<>();
		params.put("eventChannelName", "EventsSpat");
		params.put("dataTypeName", "messages");
		
		//rhEventChannelEndpoint = (RedhawkEventChannelEndpoint) component.createEndpoint("redhawk://event-channel:localhost:2809:REDHAWK_DEV?eventChannelName=EventsSpat&amp;dataTypeName=messages");
	
		context.addComponent("redhawk", component);
		
		RouteBuilder buildRoute = new RouteBuilder(){
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				//from(rhEventChannelEndpoint)
				from("redhawk://event-channel:localhost:2809:REDHAWK_DEV?eventChannelName=EventsSpat&dataTypeName=messages")
				.to("log:eventTest")
				.to("mock:result");
			}
		};
		context.addRoutes(buildRoute);
	}
	

    
    /*
     * Example test
     */
    @Test
    public void testSendMatchingMessage() throws Exception {
    	context.start();
    	Thread.sleep(10000l);
    	context.stop();
    	//String expectedBody = "<matched/>";
 
        //resultEndpoint.expectedBodiesReceived(expectedBody);
  
        //resultEndpoint.assertIsSatisfied();
      
    }
    
    /*
     * Builds route.... 
     */
    /*
     * @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start").filter(header("foo").isEqualTo("bar")).to("mock:result");
            }
        };
    }
    */
 
}

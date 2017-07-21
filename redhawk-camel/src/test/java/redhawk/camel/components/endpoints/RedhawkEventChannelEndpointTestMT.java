/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
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
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.base.RedhawkFileSystem;
import redhawk.driver.domain.RedhawkFileManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.ApplicationStartException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.testutils.RedhawkTestBase;
import redhawk.testutils.RedhawkTestUtils;

public class RedhawkEventChannelEndpointTestMT extends CamelTestSupport{
    private static Logger logger = Logger.getLogger(RedhawkEventChannelEndpointTestMT.class);
    
	@EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;
    
    @EndpointInject(uri = "mock:producerResult")
    protected MockEndpoint pResultEndpoint;
    
    @Produce(uri = "direct:start")
    protected ProducerTemplate template;
    
	private static RedhawkDriver driver; 
	
	private static RedhawkFileManager rhFS;
	
	private static RedhawkApplication rhApplication;
	
	private static String eventChannelConsumerURI, eventChannelProducerURI;
	
	//TODO: Clean this up 
	private static Boolean launchedByTest = true;
	
	@BeforeClass
	public static void setup() throws ConnectionException, MultipleResourceException, CORBAException, FileNotFoundException, IOException, ApplicationCreationException, ApplicationStartException, InterruptedException{
		RedhawkTestBase base = new RedhawkTestBase();
		
		String baseURI = "redhawk://event-channel:"+base.domainHost+":"+base.domainPort+":"+base.domainName;
		
		eventChannelConsumerURI = baseURI+"?eventChannelName=EventsSpat&dataTypeName=messages";

		eventChannelProducerURI = baseURI+"?eventChannelName=testChannel&messageId=fooBar";
		
		driver = base.driver;
		
		rhFS = driver.getDomain().getFileManager();

		//Need to make sure EventSpitter is not still left over. 
		if(!rhFS.getComponents().containsKey("/components/EventSpitter/EventSpitter.scd.xml")){
			//Run build.sh so component can have necessary files
			RedhawkTestUtils.runCommand("../demo/camel-event-channel/src/main/resources/EventSpitter", "build.sh");
			
			/*
			 * Deploy EventSpitter Component 
			 */
			RedhawkTestUtils.writeJavaComponentToCF("../demo/camel-event-channel/src/main/resources/EventSpitter", rhFS);
		}else{
			launchedByTest = false;
			logger.info("Component already deployed");
		}
		
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
        
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from(eventChannelConsumerURI)
                .to("log:eventTest")
                .to(resultEndpoint);
                
                from("direct:start")
                .to(eventChannelProducerURI)
                .to(pResultEndpoint);
            }
        };
    }
    
	@AfterClass
	public static void cleanup() throws IOException, ApplicationReleaseException, InterruptedException{
		if(rhApplication!=null)
			rhApplication.release();
		
		rhFS.removeDirectory("/waveforms/SpitToChannel");
				
		if(driver!=null){
			driver.disconnect();
		}
		
		/*
		 * Clean up component dir
		 */
		if(launchedByTest){
			//TODO: Make this dynamic
			//RedhawkTestUtils.runCommand("../demo/camel-event-channel/src/main/resources/EventSpitter", "make distclean");			
			rhFS.removeDirectory("/components/EventSpitter");		
		}
	}
}

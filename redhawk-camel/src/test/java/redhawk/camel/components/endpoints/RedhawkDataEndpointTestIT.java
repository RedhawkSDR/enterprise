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
import redhawk.testutils.RedhawkTestBase;

public class RedhawkDataEndpointTestIT extends CamelTestSupport { 
	private final String FLOAT_ROUTE_ID = "floatRoute";
	
	private static final String waveformName = "myDemo";
	
	private static final String componentName = "DataConverter_1.*";
	
	private final static String floatPortName = "dataFloat_out";
	
	private final static String doublePortName = "dataDouble_out";
	
	private final static String floatPortType = "float";
	
	private final static String doublePortType = "double";
	
	private static final String dataFloatUri = floatPortName+"&portType="+floatPortType;
	
	private final static String dataDoubleUri = doublePortName+"&portType="+doublePortType;
	
	private final static String dataShortUri = "dataShort_out&portType=short";

	private final static String dataOctetUri = "dataOctet_out&portType=octet";
	
	@EndpointInject(uri = "mock:floatResult")
    protected MockEndpoint floatResultEndpoint;
	
	@EndpointInject(uri = "mock:doubleResult")
    protected MockEndpoint doubleResultEndpoint;
	
	@EndpointInject(uri = "mock:octetResult")
    protected MockEndpoint octetResultEndpoint;
	
	@EndpointInject(uri ="mock:shortResult")
    protected MockEndpoint shortResultEndpoint;	
	
	private static RedhawkDriver driver; 

	private static RedhawkApplication rhApplication;
	
	private static RedhawkFileSystem rhFS;
	
	private static String floatEndpoint, doubleEndpoint, shortEndpoint, octetEndpoint;
	
	@BeforeClass
	public static void setup() throws ConnectionException, MultipleResourceException, CORBAException, FileNotFoundException, IOException, ApplicationCreationException, ApplicationStartException{
		RedhawkTestBase base = new RedhawkTestBase();
		
		String baseURI = "redhawk://data:"+base.domainHost+":"+base.domainPort+":"+base.domainName+"?waveformName="+waveformName+"&componentName="+componentName+"&portName=";

		//Create endpoints
		floatEndpoint = baseURI+dataFloatUri;
		doubleEndpoint = baseURI+dataDoubleUri;
		shortEndpoint = baseURI+dataShortUri;
		octetEndpoint = baseURI+dataOctetUri;
		
		driver = base.driver;
		
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
		doubleResultEndpoint.expectedMinimumMessageCount(10);
		octetResultEndpoint.expectedMinimumMessageCount(10);
		shortResultEndpoint.expectedMinimumMessageCount(10);
		floatResultEndpoint.expectedMinimumMessageCount(10);

		
		Thread.sleep(5000l);
		
		/*
		 * Ensuring that assert has been satisfied. 
		 */
		doubleResultEndpoint.assertIsSatisfied();
		octetResultEndpoint.assertIsSatisfied();
		shortResultEndpoint.assertIsSatisfied();
		floatResultEndpoint.assertIsSatisfied();
	}
	
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                /*
                 * Route to ensure can receive port data point to point
                 */
            	from(floatEndpoint)
            	.routeId(FLOAT_ROUTE_ID)
				.log("received float data")
                .to(floatResultEndpoint);
            	
            	/*
            	 * Route to ensure receiving double data works  
            	 */
            	from(doubleEndpoint)
                .log("received double data")
                .to(doubleResultEndpoint);

                /*
                 * Route to ensure receiving short data works
                 */
            	from(shortEndpoint)
                .log("received short data")
                .to(shortResultEndpoint);
            	
                /*
                 * Route to ensure receiving octet data works
                 */
            	from(octetEndpoint)
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

package redhawk.impl;

import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.xml.model.sca.scd.Provides;
import redhawk.driver.xml.model.sca.scd.Uses;

@Ignore("Using this test to make sure bidirectional ports are displayed appropriately")
public class RedhawkComponentImplTest {
	private Redhawk redhawk;
	
	private String hostName = "localhost"; 
	
	private Integer port = 2809; 

	private String domainName = "REDHAWK_DEV";

	private String applicationId = "TestBiWaveForm";
	
	private String componentId = "BiHelloWorld_1:TestBiWaveForm_1";
	
	private String sinkApplicationId = "mySinkWaveForm";
	
	private String sinkComponentId = "sinksocket_1:mySinkWaveForm_1";

	@Before
	public void setup() throws Exception{
		/*
		 * Initialize Driver
		 */
		redhawk = new RedhawkDriver(hostName, port);
		
	}
	
	@Test
	public void test() throws Exception{ 
		for(Object port : redhawk.getDomain(domainName).getApplicationByName(applicationId).getComponentByName(componentId).getSoftwareComponent().getComponentfeatures().getPorts().getProvidesAndUses()){
			if(port instanceof Uses){
				System.out.println("Uses "+((Uses)port).getUsesname()); 
			}else if(port instanceof Provides){
				System.out.println("Provides "+((Provides)port).getProvidesname()); 				
			}
		}
	}
	
	@Test
	public void testPortPerformance() throws MultipleResourceException, Exception{
		RedhawkComponent rhApplication = redhawk.getDomain(domainName).getApplicationByName(sinkApplicationId).getComponentByName(sinkComponentId); 
		
		System.out.println(new Date());
		rhApplication.getPorts(); 
		System.out.println(new Date());
	}
}

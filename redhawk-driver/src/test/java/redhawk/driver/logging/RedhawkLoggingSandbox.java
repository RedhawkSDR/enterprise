package redhawk.driver.logging;

import org.junit.Test;

import CF.UnknownIdentifier;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkLoggingSandbox extends RedhawkTestBase{
	@Test
	public void testDomainLogging(){
		try {
			String logConfig = driver.getDomain().getCorbaObj().getLogConfig();
			System.out.println(logConfig);
			System.out.println(driver.getDomain().getCorbaObj().log_level());
			System.out.println(driver.getDomain().getIdentifier());
			driver.getDomain().getCorbaObj().setLogLevel("rootLogger", 3000);
			System.out.println(driver.getDomain().getCorbaObj().log_level());
			RedhawkApplication app = driver.getDomain().createApplication("helloWorld", "/waveforms/rh/FM_RBDS_demo/FM_RBDS_demo.sad.xml");
			
			System.out.println("Appliction play");
			System.out.println(app.getCorbaObj().getLogConfig());
			System.out.println("Log Level "+app.getCorbaObj().log_level());
			app.getCorbaObj().setLogLevel("Application_impl", 3000);
			System.out.println("Log Level "+app.getCorbaObj().log_level());
			app.getCorbaObj().log_level(5000);
			System.out.println("Log Level "+app.getCorbaObj().log_level());
		} catch (MultipleResourceException | CORBAException | UnknownIdentifier | ApplicationCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

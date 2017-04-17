package redhawk;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.domain.RedhawkFileManager;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.testutils.NodeBooterProxy;

/**
 * Base test class giving you access to a RedhawkDriver that you 
 * can configure using to use jacorb by setting the system property
 * -Djacorb=true
 * 
 * A NodeBooterProxy is also available from this class giving you access to running 
 * nodeBooter commands from your test class. 
 */
public class RedhawkTestBase {
	public static Logger logger = Logger.getLogger(RedhawkTestBase.class.getName());
	
	public static RedhawkDriver driver; 
	
	public static NodeBooterProxy proxy;
	
	public static String sdrRoot = "/var/redhawk/sdr";
	
	public static String deviceManagerHome = sdrRoot+"/dev";
	
	@BeforeClass
	public static void setupB4Class(){
		logger.info("Jacorb prop is: "+System.getProperty("jacorb"));
		Boolean jacorbTest = Boolean.parseBoolean(System.getProperty("jacorb", "false"));
		
		if(jacorbTest){
			logger.info("Testing with jacorb");
			Properties props = new Properties(); 
			props.put("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
			props.put("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");
			driver = new RedhawkDriver("127.0.0.1", 2809, props);
		}else{
			logger.info("Testing with default orb for JDK");			
			driver = new RedhawkDriver(); 
		}
		
		//Create proxy utility 
		proxy = new NodeBooterProxy();
	}
	
	@AfterClass
	public static void afterClass() throws MultipleResourceException, CORBAException, ApplicationReleaseException{
		if(driver!=null){
			for(RedhawkApplication application : driver.getDomain().getApplications()){
				//Clean up applications
				application.release();
			}
			driver.disconnect();
		}
		//Always make sure you delete waveforms you create
		//TODO: Clean up this logic
		try {
			RedhawkFileManager manager = driver.getDomain().getFileManager();
			if(!manager.findDirectories("/waveforms/testWaveform").isEmpty())
				manager.removeDirectory("/waveforms/testWaveform");
		} catch (ConnectionException | IOException | CORBAException e) {
			logger.info("Unable to delete wavemform likely cause it doesn't exist.");
		}
	}
}

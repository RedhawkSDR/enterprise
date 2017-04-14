package redhawk.camel.components.endpoints;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.domain.RedhawkFileManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.properties.RedhawkProperty;
import redhawk.driver.properties.RedhawkSimple;

//TODO: Clean this up!!!!!
public class RedhawkFileEndpointTestIT extends CamelTestSupport{
	private static Logger logger = Logger.getLogger(RedhawkFileEndpointTestIT.class);
	
	//TODO: Use base test class to do this.
	private static RedhawkDriver driver; 
	
	private static RedhawkFileManager fileManager;
	
	@EndpointInject(uri="redhawk://file-manager:localhost:2809:REDHAWK_DEV?directory=data")
	RedhawkFileEndpoint redhawkFileProducerEndpoint; 
	
	@EndpointInject(uri="redhawk://file-manager:localhost:2809:REDHAWK_DEV?directory=data-out&delete=true")
	RedhawkFileEndpoint redhawkFileConsumerEndpoint; 
	
	private static String resourcesDirectory;
	
	private static String testOutputDirectory;
	
	@BeforeClass
	public static void setup() throws ConnectionException, MultipleResourceException, CORBAException{
		File file = new File("src/test/resources/data");
		
		File testOutput = new File("src/test/resources/data-processed");
		
		resourcesDirectory = file.getAbsolutePath();
		
		testOutputDirectory = testOutput.getAbsolutePath();
		
		driver = new RedhawkDriver();
		
		fileManager = driver.getDomain().getFileManager();
		
		List<String> dataDir = fileManager.findDirectories("data");
		//assertTrue(dataDir.isEmpty());
	}
	
	class ProcessData implements Callable{
		RedhawkDriver driver;  
		
		public ProcessData(RedhawkDriver driver){
			this.driver = driver; 
		}

		@Override
		public Object call() throws Exception {
			//Once file is in place for file reader launch waveform to do processing
			try {
				RedhawkApplication application = driver.getDomain().createApplication("noaaProcessor", new File("src/test/resources/waveforms/camelFileTestWaveform/camelFileTestWaveform.sad.xml"));
				application.start();
				RedhawkComponent frComponent = application.getComponentByName("FileReader.*");
				Map<String, RedhawkProperty> propertiesMap = frComponent.getProperties();
				RedhawkSimple sourceUriProp = (RedhawkSimple) propertiesMap.get("source_uri");
				RedhawkSimple playbackStateProp = (RedhawkSimple) propertiesMap.get("playback_state");
				sourceUriProp.setValue("sca://data/noaa.dat");
				playbackStateProp.setValue("PLAY");
				logger.info("Created waveform should now be in play state.");
				Thread.sleep(15000l);
				logger.info("Giving time to process data");
				application.release();
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}
	/*
	 * Read in data from src/test/resources and 
	 * make it accessible to your REDHAWK Domain
	 */
	@Test
	public void testFileEndpointProducer() throws InterruptedException{
		/*
		exec.submit(new ProcessData(driver));
		exec.shutdown();
		exec.awaitTermination(30l, TimeUnit.SECONDS);
		*/
		Thread.sleep(5000l);
		
		try {
			//TODO: Refactor use an exector 
			RedhawkApplication application = driver.getDomain().createApplication("noaaProcessor", new File("src/test/resources/waveforms/camelFileTestWaveform/camelFileTestWaveform.sad.xml"));
			application.start();
			RedhawkComponent frComponent = application.getComponentByName("FileReader.*");
			Map<String, RedhawkProperty> propertiesMap = frComponent.getProperties();
			RedhawkSimple sourceUriProp = (RedhawkSimple) propertiesMap.get("source_uri");
			RedhawkSimple playbackStateProp = (RedhawkSimple) propertiesMap.get("playback_state");
			sourceUriProp.setValue("sca://data/noaa.dat");
			playbackStateProp.setValue("PLAY");
			logger.info("Created waveform should now be in play state.");
			Thread.sleep(20000l);
			logger.info("Giving time to process data");
			application.release();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Make sure the two files are now written
		assertEquals("Should now be two files in sca://data", 2, fileManager.findFilesInDirectory("data", ".*").size());
		assertTrue("Should now be zero files in sca://data-out", fileManager.findFilesInDirectory("data-out", ".*").isEmpty());
	}
	
	@Override
    protected RouteBuilder createRouteBuilder() {
		 return new RouteBuilder() {
			 public void configure() {
				 /*
				  * Read data from resources directory and put it in $SDRROOT/dom/data
				  */
				 from("file://"+resourcesDirectory)
				 .log("Received Data ")
				 .to(redhawkFileProducerEndpoint);
				 
				 
				 /*
				  * Pick data up from Waveform output
				  */
				 from(redhawkFileConsumerEndpoint)
				 .log("Received Processed Data")
				 .to("file://"+testOutputDirectory);
			 }
		 };
	}
	
	@AfterClass
	public static void cleanup() throws IOException{
		File camelDir = new File("src/test/resources/data/.camel");
		File dataDir = new File("src/test/resources/data");
		for(File file : camelDir.listFiles()){
			FileUtils.moveFileToDirectory(file, dataDir, false);
			logger.info("Moving file "+file+" Base to original directory "+dataDir);
		}
		
		//Delete .camel directory
		FileUtils.deleteDirectory(camelDir);
		
		/*
		 * Clean up CF directories
		 */
		fileManager.removeDirectory("/data");
		fileManager.removeDirectory("/data-out");
		fileManager.removeDirectory("/waveforms/camelFileTestWaveform");
		
		//Remove data processed directory
		FileUtils.deleteDirectory(new File("src/test/resources/data-processed"));
	}
}

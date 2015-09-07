package redhawk.websocket.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.properties.RedhawkSimple;

public class RedhawkTestUtil {
	public static String sampleWebSocketPortEndpoint(String portName){
		String ip = System.getProperty("redhawk.host", "localhost");
		Integer port = Integer.parseInt(System.getProperty("redhawk.port", ""+2809));
		String redbusIp = System.getProperty("redbus.host", "localhost");
		Integer jettyPort = Integer.valueOf(System.getProperty("jetty.port", "8781"));
		
		String wsEndpoint = "ws://"+redbusIp+":"+jettyPort+"/redhawk/"
				+ ip
				+ ":"+port+"/domains/REDHAWK_DEV/applications/Websocket.*/components/SigGen.*/ports/"+portName;
		
		return wsEndpoint;
	}
	
	public static String sampleWebSocketEventChannel(String eventType){
		String ip = System.getProperty("redhawk.host", "localhost");
		Integer port = Integer.parseInt(System.getProperty("redhawk.port", ""+2809));
		String redbusIp = System.getProperty("redbus.host", "localhost");
		Integer jettyPort = Integer.valueOf(System.getProperty("jetty.port", "8781"));
		
		String wsEndpoint = "ws://"+redbusIp+":"+jettyPort+"/redhawk/"
				+ ip
				+ ":"+port+"/domains/REDHAWK_DEV/eventchannels/ODM_Channel?messageType="+eventType;
		
		return wsEndpoint;
	}
	
	public static RedhawkApplication launchApplication(boolean start) throws Exception{
		File waveForm;
		
		String ip = System.getProperty("redhawk.host", "localhost");
		Integer port = Integer.parseInt(System.getProperty("redhawk.port", ""+2809));
		
		Redhawk redhawk = new RedhawkDriver(ip, 2809);
		Integer majorVersion = getRHMajorVersion(); 
		RedhawkApplication application; 
		if (majorVersion >= 2) {
			waveForm = new File(
					"src/test/resources/waveform/2.0.0/wf-integration-test.sad.xml");
			try {
				application = redhawk.getDomain("REDHAWK_DEV").createApplication("WebsocketTest", waveForm);
			} catch (Exception ex) {
				application = redhawk.getDomain("REDHAWK_DEV").createApplication("WebsocketTest", "/waveforms/wf-integration-test/wf-integration-test.sad.xml");
			}
		} else {
			waveForm = new File(
					"src/test/resources/waveform/1.10/websocket-integration-test.sad.xml");
			try {
				application = redhawk.getDomain("REDHAWK_DEV").createApplication("WebsocketTest",waveForm);
			} catch (Exception ex) {
				application = redhawk.getDomain("REDHAWK_DEV").createApplication("WebsocketTest","/waveforms/websocket-integration-test/websocket-integration-test.sad.xml");
			}
		}
		
		if(start)
			application.start();
		
		return application;
	}	
	
	public static Boolean redhawkDevExists(){
		String ip = System.getProperty("redhawk.host", "localhost");
		Integer port = Integer.parseInt(System.getProperty("redhawk.port", ""+2809));
		
		Redhawk redhawk = new RedhawkDriver(ip, port);
		Boolean rhDevExists = false;
		try{
			if(redhawk.getDomain("REDHAWK_DEV")!=null);	
				rhDevExists = true;
		}catch(Exception ex){
			
		}
		
		return rhDevExists;
	}
	
	public static Integer getRHMajorVersion() throws Exception{
		String ip = System.getProperty("redhawk.host", "localhost");
		Integer port = Integer.parseInt(System.getProperty("redhawk.port", ""+2809));
		
		Redhawk redhawk = new RedhawkDriver(ip, port);
		RedhawkSimple version = redhawk.getDomain("REDHAWK_DEV")
				.getProperty("REDHAWK_VERSION");
		Integer majorVersion = Integer.valueOf(version.getValue().toString()
				.substring(0, 1));
		
		return majorVersion;
	}
	
	public static Boolean redbusIsRunning(){
    	Boolean running = false; 
		try {
    	    String line;
    	    Process p = Runtime.getRuntime().exec("ps -ef");
    	    BufferedReader input =
    	            new BufferedReader(new InputStreamReader(p.getInputStream()));
    	    while ((line = input.readLine()) != null) {
    	        if(line.contains("redbus.base"))
    	        	running = true; 
    	    }
    	    input.close();
    	} catch (Exception err) {
    	    err.printStackTrace();
    	}
		
		return running; 
	}
}

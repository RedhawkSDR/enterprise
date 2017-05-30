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
package redhawk.websocket.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.properties.RedhawkSimple;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkTestUtil extends RedhawkTestBase{
	public static String sampleWebSocketPortEndpoint(String portName){
		String ip = System.getProperty("redhawk.host", "localhost");
		Integer port = Integer.parseInt(System.getProperty("redhawk.port", ""+2809));
		String karafIp = System.getProperty("karaf.host", "localhost");
		Integer jettyPort = Integer.valueOf(System.getProperty("jetty.port", "8781"));
		
		String wsEndpoint = "ws://"+karafIp+":"+jettyPort+"/redhawk/"
				+ ip
				+ ":"+port+"/domains/REDHAWK_DEV/applications/Websocket.*/components/SigGen.*/ports/"+portName;
		
		return wsEndpoint;
	}
	
	public static String sampleWebSocketEventChannel(String eventType){
		String ip = System.getProperty("redhawk.host", "localhost");
		Integer port = Integer.parseInt(System.getProperty("redhawk.port", ""+2809));
		String karafIp = System.getProperty("karaf.host", "localhost");
		Integer jettyPort = Integer.valueOf(System.getProperty("jetty.port", "8781"));
		
		String wsEndpoint = "ws://"+karafIp+":"+jettyPort+"/redhawk/"
				+ ip
				+ ":"+port+"/domains/REDHAWK_DEV/eventchannels/ODM_Channel?messageType="+eventType;
		
		return wsEndpoint;
	}
	
	public static RedhawkApplication launchApplication(boolean start) throws Exception{
		File waveForm;
				
		RedhawkApplication application; 
		waveForm = new File("src/test/resources/waveform/wf-integration-test.sad.xml");
		try {
			application = driver.getDomain().createApplication("WebsocketTest", waveForm);
		} catch (Exception ex) {
			application = driver.getDomain().createApplication("WebsocketTest", "/waveforms/wf-integration-test/wf-integration-test.sad.xml");
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
	
	public static Boolean karafIsRunning(){
    	Boolean running = false; 
		try {
    	    String line;
    	    Process p = Runtime.getRuntime().exec("ps -ef");
    	    BufferedReader input =
    	            new BufferedReader(new InputStreamReader(p.getInputStream()));
    	    while ((line = input.readLine()) != null) {
    	        if(line.contains("karaf.base"))
    	        	running = true; 
    	    }
    	    input.close();
    	} catch (Exception err) {
    	    err.printStackTrace();
    	}
		
		return running; 
	}
}

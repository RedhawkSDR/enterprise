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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.omg.CORBA.Any;
import org.ossie.properties.AnyUtils;

import com.google.gson.Gson;

import CF.DataType;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.eventchannel.listeners.PropertyChange;
import redhawk.driver.eventchannel.listeners.PropertyChangeListener;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.properties.RedhawkStructSequence;
import redhawk.websocket.model.PropertyChangeModel;

/**
 * Support class for launching waveform and controlling it via the console. 
 *
 */
public class RedhawkIntegrationSupport {
	public static void main(String[] args) throws Exception{
		RedhawkApplication application = RedhawkTestUtil.launchApplication(false);
	    Scanner scanner = new Scanner(System.in);
	    System.out.println("Enter exit to quit:");
	    Boolean dontStopBelieving = true;
	    while(dontStopBelieving){
	    	String input = scanner.next();
	    	if(input.equals("exit")){
	    		dontStopBelieving=false;
	    	}else{
	    		System.out.println("Entered: "+input);
	    	}
	    }
		application.release();
	}
	
	/**
	 * Method used to connect to EventChannels. 
	 * 
	 * @throws ConnectionException
	 * @throws Exception
	 */
	public void eventChannelConnect() throws ConnectionException, Exception{
		RedhawkDriver driver = new RedhawkDriver("localhost", 2809);
		Gson gson = new Gson(); 
		System.out.println(driver.getDomain("REDHAWK_DEV").getEventChannelManager().eventChannels());
		
		String path = "localhost:2809/domains/REDHAWK_DEV/eventchannels/ODM_Channel?messageType=propertyChanges ";
		path = path.replaceFirst("redhawk/", "");
        String[] pathArray = path.split("/");
        for(int i =0; i<pathArray.length; i++){
        	System.out.println("index: "+i+" entry: "+pathArray[i]);
        }
        
        driver.getDomain("REDHAWK_DEV").getEventChannelManager().getEventChannel("ODM_Channel").subscribe(new PropertyChangeListener() {
            @Override
            public void onMessage(PropertyChange message) {
            	PropertyChangeModel model = new PropertyChangeModel(); 
                model.setSourceId(message.getSourceId());
                model.setSourceName(message.getSourceName());
                Map<String, Object> properties = new HashMap<String, Object>();
                for(Entry<String, Object> entry : message.getProperties().entrySet()){
                	//if(entry.getValue() instanceof DataType){
                	if(entry.getValue() instanceof DataType[]){
                		DataType[] dt = (DataType[]) entry.getValue();
                		Map<String, Object> test = new HashMap<>(); 
                		for(DataType t : dt){
                	        Object propertyValue = AnyUtils.convertAny(t.value);
                	        test.put(t.id, propertyValue);	
                		}
                		properties.put(entry.getKey(), test);
                	}else if(entry.getValue() instanceof Any[]){
                		Any[] anyObject = (Any[]) entry.getValue();
                        //RedhawkStructSequence sequence = new RedhawkStructSequence(driver.getOrb(), "bs", entry.getKey(), anyObject);
                        RedhawkStructSequence sequence = new RedhawkStructSequence(null, "bs", entry.getKey(), anyObject);
                		properties.put(entry.getKey(), sequence.toListOfMaps());
                	}else{
                		properties.put(entry.getKey(), entry.getValue());
                	}
                }
                model.setProperties(properties);
                try{
                	System.out.println("JSON: "+gson.toJson(model));
                }catch(Exception ex){
                	ex.printStackTrace();
                }  
          }
        });
        TimeUnit.MINUTES.sleep(2);
        System.out.println("Done!");
	}
	
}

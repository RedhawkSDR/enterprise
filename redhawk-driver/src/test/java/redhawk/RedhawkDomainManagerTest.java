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
package redhawk;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import BULKIO.dataFloatOperations;
import BULKIO.dataFloatPOATie;
import CF.Application;
import CF.ApplicationFactory;
import CF.DataType;
import CF.DeviceAssignmentType;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.RedhawkUtils;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.application.RedhawkApplicationFactory;
import redhawk.driver.bulkio.BulkIOData;
import redhawk.driver.bulkio.Packet;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.devicemanager.DeviceManagerInturruptedCallback;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.eventchannel.RedhawkEventChannel;
import redhawk.driver.eventchannel.listeners.PropertyChange;
import redhawk.driver.eventchannel.listeners.PropertyChangeListener;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.PortListener;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.properties.RedhawkSimple;
import redhawk.driver.properties.RedhawkSimpleSequence;
import redhawk.driver.xml.ScaXmlProcessor;
import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.spd.Softpkg;

@Ignore("Figure out if there's some things in this test we should save...")
public class RedhawkDomainManagerTest {
    
    RedhawkDomainManager domainManager;
    
    private RedhawkDriver driver;
    
    private String domainName = "REDHAWK_DEV";
    
    private ORB orb;
    @Before()
    public void setup(){
        try {
            domainManager = driver.getDomain(domainName);
            orb = driver.getOrb();
        } catch (ConnectionException | ResourceNotFoundException | CORBAException e ) {
            e.printStackTrace();
        }     
    }
    
    @Test
    public void testSetMe() throws Exception {
    	//TODO: Figure out what was trying to be done below...
    	//Object objRef = orb.resolve_initial_references("NameService");
        //NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
        String domainManagerUrl = domainName+"/"+domainName;
    	
    	RedhawkDriver driver = new RedhawkDriver("127.0.0.1", 2809);
    	RedhawkDomainManager mgr = driver.getDomain(domainName);
    	mgr.getPropertyConfiguration();

    	System.out.println(mgr.getName());
    	
    	RedhawkPort comp = driver.getPort("REDHAWK_DEV/TestWF.*/FileReader.*/dataFloat_out");
    	comp.connect(new PortListener<float[]>(){
			@Override
			public void onReceive(Packet<float[]> packet) {
				System.out.println("HERE");
			}
    	});
    	
    	
    	driver.getPort("REDHAWK_DEV/application/component/port/myPort").connect(new PortListener<float[]>() {
			@Override
			public void onReceive(Packet<float[]> packet) {
				
			}
		});
    	
     	InputStream s = new FileInputStream(new File("/var/redhawk/sdr/dom/components/DataConverter/DataConverter.spd.xml"));
    	
    	Softpkg spd = ScaXmlProcessor.unmarshal(s, Softpkg.class);
    	
    	System.out.println("TYPE: " + spd.getType());
    	
    	RedhawkDeviceManager dmg = driver.getDomain("REDHAWK_DEV").createDeviceManager("RYAN", "/tmp", true, new DeviceManagerInturruptedCallback(){

			@Override
			public void deviceManagerReconnected() {
				System.out.println("RECONNECTED!!!");
			}

			@Override
			public void deviceManagerDisconnected() {
				System.out.println("DISCONNECTED!!!");				
			}

			@Override
			public void connectionProblem(String message) {
				System.out.println("CONNECTION PROBLEM");
			}

			@Override
			public void lostDomainManagerConnection() {
				System.out.println("LOST DOMAIN");
			}

			@Override
			public void restoredDomainManagerConnection() {
				System.out.println();
			}

			@Override
			public void serviceReregistrationFailed(String message,
					Object serviceInformation, Class serviceInformation2,
					Class serviceInformation3, NotFound e) {
				System.out.println("Service Registration Failed...");
			}
    		
    	});

    	Thread.currentThread().sleep(40000);
    	
    	BulkIOData<float[]> pipeline = new BulkIOData<>(new PortListener<float[]>() {
			@Override
			public void onReceive(Packet<float[]> packet) {
				// TODO Auto-generated method stub
			}
		});
    	
    	dmg.registerService("TEST", pipeline, dataFloatPOATie.class, dataFloatOperations.class);
    	Thread.currentThread().sleep(40000);
    	
    	System.out.println("DONE");
    	
    	
    	driver.getDomain("REDHAWK_DEV").unRegisterAllDriverRegisteredDeviceManagers();
    	
    	List<RedhawkApplication> applications = driver.getDomain("<insert domain>").getApplications();
    	System.out.println("DONE");

    	for(RedhawkApplication app : applications){
    		app.getAssembly();
    	}
    		
    	System.out.println("DONE2");
    	
    	/*values.put("gain::gain_value", 9.0d);
    	values.put("gain::tuner_number", Integer.parseInt("1"));
        
    	try {
    		struct.setValues(values);
    	} catch(Throwable t){
    		System.out.println("CAUGHT SOMETHING");
    	}*/
    	
    	
    	RedhawkComponent componentA = driver.getComponent("");
    	RedhawkComponent componentB = driver.getComponent("");
    	
    	RedhawkPort port = componentA.getPort("outputPort");
    	
    	/*TODO: Not sure below has a point
    	 * if(port instanceof RedhawkUsesPortImpl){
    		
    	}
    	
    	if(port instanceof RedhawkProvidesPortImpl){
    		
    	}
    	*/
    	
    	
    	
        try {
        	//connect to a redhawk domain
        	 driver = new RedhawkDriver("127.0.0.1", 2809);

        	driver.getPort("REDHAWK_DEV/application/component/port/myPort").connect(new PortListener<float[]>() {
				@Override
				public void onReceive(Packet<float[]> packet) {
					
				}
			});
        	
        	RedhawkApplication application = driver.getApplication("");
        	
        	        	
//        	RedhawkDomainManager domainManager = domainManager = driver.getDomain("REDHAWK_DEV");
//        	RedhawkApplicationFactory factory = domainManager.createApplicationFactory(waveform);
//        	application = factory.createApplication(instanceName, initialConfiguration, deviceAssignments)
        	
        	
        } catch (ConnectionException e) {
            //handle this
        }    	
    	
        	driver.getDomain("REDHAWK_DEV").getApplications();

        	RedhawkDomainManager manager = driver.getDomain("REDHAWK_DEV");
        	RedhawkSimple simple = manager.getProperty("COMPONENT_BINDING_TIMEOUT");
        	simple.setValue(25);

        	RedhawkComponent component = driver.getComponent("REDHAWK_DEV/audioDemo.*/TESTComp.*");
        	RedhawkSimpleSequence seq = component.getProperty("TEST");
        	System.out.println("SIZE: " + seq.getValues().size());
        	System.out.println("SIZE: " + seq.getValues().size());
        	seq.addValue(354);

        	port = driver.getPort("PRIMALFUSION/primalfusion2_wideband.*/EnergyDetect.*/PSD_out");
        	
        	port.connect(new PortListener<float[]>() {
				@Override
				public void onReceive(Packet<float[]> packet) {
					System.out.println("RECEIVED");
				}
			});
        	        	
        	       
        	port = driver.getPort("REDHAWK_DEV/audioDemo.*/FileReader.*/dataFloat_out");
        	
        	
        	port.connect(new PortListener<float[]>() {

				@Override
				public void onReceive(Packet<float[]> packet) {
					System.out.println("FLOAT RECIEVED: " + packet.getData().length);
				}
        		
			});
        	
        	Thread.currentThread().sleep(30000);
        	port.disconnect();
        	System.out.println(driver.getDomain("REDHAWK_DEV").getFileManager().findFilesInDirectory("/data/stations", ".*.16tr"));
        	
        	
//        	RedhawkApplicationFactory factory = driver.getDomain("REDHAWK_DEV").createApplicationFactory("/waveforms/audioDemo/audioDemo.sad.xml");
//        	RedhawkApplication radio = factory.createApplication("REDHAWK Radio", null, null);
//    		
//    		if(!radio.isStarted()) { 
//    			radio.start();
//    		}
//    	
//    		System.out.println("SLEEPING .....");
//    		Thread.currentThread().sleep(20000);
//    	
//    		
//    		if(radio.isStarted()) { 
//    			radio.stop();
//    		}        	
//        	
//    		System.out.println("SLEEPING 2 .....");
//    		Thread.currentThread().sleep(20000);    		
//    		
//    		if(!radio.isStarted()) { 
//    			radio.start();
//    		}        
    	
    	
//    	if(fact == null){
//    		fact = domainManager.createApplicationFactory("/waveforms/SigTest/SigTest.sad.xml");
//    	}
//    	
//    	FileInputStream fis = new FileInputStream(new File("/var/redhawk/sdr/dom/components/SigGenComponent/SigGenComponent.spd.xml"));
//    	Softpkg pkg = ScaXmlP	rocessor.unmarshal(fis, Softpkg.class);
//    	System.out.println(pkg.getDescriptor().getLocalfile().getName());
    	
//    	RedhawkDeviceManager deviceManager = domainManager.createDeviceManager("RYAN", "/tmp", true);
    	
    	
//    	System.out.println("BEFORE: " + domainManager.getApplications().size());
//    	RedhawkApplication application = domainManager.getApplicationByName("SigGen.*");
//    	System.out.println(application.getIdentifier());
//    	Thread.currentThread().sleep(40000);
//    	System.out.println("AFTER & UP: " + domainManager.getApplications().size());
//    	System.out.println(application.getIdentifier());
//    	System.out.println(application.getName());

    	
    	
//    	System.out.println(fact);
//    	
//    	System.out.println(domainManager.getDeviceManagerByName("DevMgr.*").getDeviceByName("GPP.*").getPorts().size());
    	
//    	FileInputStream fis = new FileInputStream(new File("/var/redhawk/sdr/dom/waveforms/SigGenComponentWF/SigGenComponentWF.sad.xml"));
//    	Softwareassembly pkg = ScaXmlProcessor.unmarshal(fis, Softwareassembly.class);
    	
//    	System.out.println(domainManager.getEventChannels());
    	
//    	System.out.println(pkg.getId());
    	
//    	System.out.println(domainManager.getFileManager().getWaveforms());
    	
//    	
//    	//Change a Simple Property
//    	RedhawkComponent component = domainManager.getApplicationByName("waveform_.*").getComponentByName("comp_1.*");
//    	RedhawkSimple simp = component.getProperty("Bandwidth");
//    	simp.setValue("140000");
//
//    	
//    	//Add a Struct to a Struct Sequence
//    	RedhawkStructSequence redhawkStructSequence = domainManager.getApplicationByName("SigGen.*").getComponentByName("SigGen.*").getProperty("TEST_SEQ");
//    	Map<String, Object> structToAdd = new HashMap<String, Object>();
//    	structToAdd.put("SomeProp", "some value");
//    	structToAdd.put("Another Prop", 200000f);
//    	redhawkStructSequence.addStructToSequence(structToAdd);    	    	
//    	
//    	
//    	
//    	
//    	//Create and launch a waveform from a file
//    	File waveformFile = new File("/tmp/mywaveform.sad.xml");
//    	RedhawkApplicationFactory appFactory = domainManager.createApplicationFactory(waveformFile);
//    	//create application with instance name, initialconfiguration, and device assignments
//    	appFactory.createApplication("App1", null, null);
//    	
//    	
//    	
//    	
//    	//Create and launch a waveform that was generated programmatically
//    	Softwareassembly waveform = new Softwareassembly();
//    	//build custom waveform or load from XML 
//    	RedhawkApplicationFactory appFactory2 = domainManager.createApplicationFactory(waveform);
//    	//create application with instance name, initialconfiguration, and device assignments
//    	appFactory.createApplication("App2", null, null);
//    	
//    	
//    	
//    	
//    	//Create and launch a waveform from a file in SDRROOT
//    	RedhawkApplicationFactory appFactory3 = domainManager.createApplicationFactory("/my_waveform_in_sdrroot.sad.xml");
//    	//create application with instance name, initialconfiguration, and device assignments
//    	appFactory.createApplication("App3", null, null);
//    	
//    	
//    	
//    	
//    	
//    	
//    	
//    	//Consume data from a REDHAWK Component Port
//    	RedhawkComponent someOtherComponent = domainManager.getApplicationByName("waveform_.*").getComponentByName("other_component.*");
//    	RedhawkPort port = someOtherComponent.getPort("dataFloat_out");
//    	port.connect(new PortListener<float[]>() {
//			@Override
//			public void onReceive(Packet<float[]> packet) {
//				//do something with the data
//			}	
//		});    	
//    	
//
//    	
//    	//Consuming messages from an event channel
//    	domainManager.subscribeToEventChannel("someEventChannel", new MessageListener() {
//			@Override
//			public void onMessage(Map<String, Object> message) {
//				//do something with the message
//			}
//		});
//    	
//    	
//    	
//    	//Consuming property change events from an event channel
//    	domainManager.subscribeToEventChannel("aPropertyChangeEventChannel", new PropertyChangeListener() {
//			@Override
//			public void onMessage(PropertyChange message) {
//				System.out.println(message.getSourceId() + ":" + message.getSourceName() + ":" + message.getProperties() );
//			}
//		});
//
//    	
//    	
//    	//Sending to an event channel
//    	Map<String,Object> messageToSend = new HashMap<String, Object>();
//    	messageToSend.put("property", "somevalue");
//    	domainManager.publishToEventChannel("someMessageId", "someEventChannel", messageToSend);
//    	
//    	
    	
//    	
//    	for(RedhawkProperty prop : component.getProperties().values()){
//    		System.out.println(prop.getClass().getName());
//    	}
//    	simple.setValue(new Float(140000));

//    	for(RedhawkPort port : component.getPorts()){
//    		System.out.println(port.getPortName());
//    		System.out.println(port.getPortType());
//    	}
//    	System.out.println("IN HERE");
//    	System.out.println("HELLO " + component.getName());
//    	RedhawkPort port = component.getPort("fft_dataFloat_out");
//    	
//    	System.out.println(driver.getOrb().object_to_string(port.getCorbaObject()));
//    	
//    	port.connect(new PortListener<float[]>() {
//			@Override
//			public void onReceive(Packet<float[]> packet) {
//				System.out.println(packet.getData().length);
//				
//				for(float f : packet.getData()){
//					System.out.println(f);
//				}
//				
//			}	
//		});
//    	
//    	Thread.currentThread().sleep(60000);
//    	System.out.println(component.getPorts());   	
//    	System.out.println(domainManager.getApplicationByName("SigGen.*").getComponentByName("SigGen.*").getPorts().size());
////    	System.out.println(driver.getDomainManagers().size());
//    	System.out.println("HERE");
//    	RedhawkStructSequence redhawkStructSequence = domainManager.getApplicationByName("SigGen.*").getComponentByName("SigGen.*").getProperty("TEST_SEQ");
//    	
//    	
//    	Map<String, Object> structToAdd = new HashMap<String, Object>();
//    	structToAdd.put("A", "<value>");
//    	structToAdd.put("B", "<value>");
//    	System.out.println("HERE");
//    	redhawkStructSequence.addStructToSequence(structToAdd);
    	
//    	for(String s : domainManager.getDeviceManagerByName("DevMgr.*").getFileSystem().findDirectoriesInDirectory("/nodes/", ".*")){
//    		System.out.println(s);
//    	}
    	
    	
//    	RedhawkApplication application = domainManager.getApplicationByName("psk_efd.*");
//    	RedhawkComponent component = application.getComponentByName("Baudrateline.*");
//    	RedhawkPort port = component.getPort("dataFloat_out");
//        port.connect(new PortListener<float[]>() {
//			@Override
//			public void onReceive(Packet<float[]> packet) {
//				if(packet.isNewSri()){
//					System.out.println("New SRI From Client" + packet.isNewSri());
//				}
//			}
//		});
//        
//    	Thread.currentThread().sleep(30000);
    	
//    	port.disconnect();
    }
    
    
    @Test
    public void testGetEnergyDetect() throws Exception {
    	RedhawkEventChannel channel = domainManager.getEventChannelManager().getEventChannel("<eventChannelName>");
    	channel.subscribe(new PropertyChangeListener() {
			@Override
			public void onMessage(PropertyChange message) {
				System.out.println(message.getSourceId() + ":" + message.getSourceName() + ":" + message.getProperties() );				
			}
		});
    	
    	Thread.currentThread().sleep(30000);
    }
    
    
    @Test
    public void testPublishMessage() throws Exception {
    	Map<String, Object> message = new HashMap<String, Object>();
    	message.put("HI", "THERE");
    	
    	for(int i=0;i<100;i++){
    		domainManager.getEventChannelManager().getEventChannel("<eventChannelName>").publish("tipping-queue-test", message);
    		Thread.currentThread().sleep(500);	
    	}
    }
        
    
    
    @Test
    public void testGetRedhawkDomainName() throws Exception {
        domainManager.getName();
        domainManager.getDevices();
        domainManager.getIdentifier();
        assertEquals(domainManager.getCorbaObj().name(), domainName);
    }
    
    @Test
    public void testGetApplicationFactories() throws Exception {
    	//TODO: Method no longer present...
    	//for(RedhawkApplicationFactory appFact : domainManager.getApplicationFactories()){
    	//	System.out.println(appFact.getName());
    	//}
    }
    
    @Test
    public void testWriteFileToRedhawk() throws Exception {
    	//TODO: Method no longer present...
    	//RedhawkUtils.writeFileToRedhawk(domainManager.getFileManager(), srcFile, destFile);
    }
    
    
    @Test
    public void testCreateApplicationFactory() throws Exception {
        domainManager.createApplication("myApplication", "/waveforms/testPropsWaveform/testPropsWaveform.sad.xml").start();
        RedhawkApplicationFactory appFactory = domainManager.getApplicationFactoryByIdentifier("/GPS_Post-D_Controller_wf.sad.xml");
        //TODO: Method no longer present
        //appFactory.createApplication("TEST", null, null);
        //TODO: Method no longer preset
        //domainManager.installApplicationFromSDR("/<waveform name>", false);
        
        for(ApplicationFactory fact : domainManager.getCorbaObj().applicationFactories()){
            System.out.println(fact.name());
            Application app = fact.create("<value>", new DataType[]{}, new DeviceAssignmentType[]{});
            app.start();    
            
        }
        
        
        assertEquals(domainManager.getCorbaObj().name(), domainName);
    }
    
    
}
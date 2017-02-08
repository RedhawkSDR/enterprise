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
package redhawk.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import BULKIO.UsesPortStatistics;
import BULKIO.UsesPortStatisticsProvider;
import BULKIO.UsesPortStatisticsProviderHelper;
import CF.Port;
import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.bulkio.Packet;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.port.PortListener;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.port.RedhawkPortStatistics;

@Ignore("Test getting provides port data")
public class RedhawkProvidesPortImplTest {	
	List<RedhawkPort> ports;  
	
	private Redhawk redhawk;
	
	private String hostName = "localhost"; 
	
	private Integer port = 2809; 
	
	private String domainName = "REDHAWK_DEV";
	
	private String applicationId = "myWaveform";
	
	private String sinkApplicationId = "mySinkWaveForm";
	
	private String playApplicationId = "PlayProject"; 
	
	private String componentId = "HardLimit_1:myWaveform_1";
	
	private String dumpComponentId = "SRIDump_1:SRIDump_waveform_1";
	
	private String sinkComponentId = "sinksocket_1:mySinkWaveForm_1";
	
	private String playComponentId = "HardLimit_1:PlayProject_1";

	private String dumpApplicationId = "SRIDump_waveform";

	private String portNameUses = "dataDouble_out";
	
	private String portNameProvides = "dataDouble_in";
	
	private ArrayList<Packet<double[]>> dataList = new ArrayList<>(); 
	
	private Packet<double[]> rhPackage = null; 


	@Before
	public void setup() throws Exception{ 
		ports = new ArrayList<>();
		
		/*
		 * Initialize Driver
		 */
		redhawk = new RedhawkDriver(hostName, port);
		
		RedhawkApplication rhApplication = redhawk.getDomain(domainName).getApplicationByIdentifier(applicationId); 
		
		/*List<RedhawkComponent> components = rhApplication.getComponents();
		
		for(RedhawkComponent component : components){
			System.out.println(component.getName());
			
			ports.addAll(rhApplication.getComponentByName(component.getName()).getPorts());
		}*/
	}
	
	/*
	 * Sample code for listening to a waveform 
	 */
	@Test
	public void testUses() throws Exception{
		RedhawkPort port = redhawk.getDomain(domainName).getApplicationByName(applicationId).getComponentByName(componentId).getPort(portNameUses);
		
		port.connect(new PortListener<double[]>(){

			@Override
			public void onReceive(Packet<double[]> packet) {
				System.out.println("Receiving data");
				System.out.println(packet.getSriAsMap());
				System.out.println("PACKET Fractional "+packet.getTfsec());
				System.out.println("System NANO "+System.nanoTime());
				System.out.println("PACKET WHOLE Sec: "+packet.getTwsec());
				System.out.println("System Whole "+(double)System.currentTimeMillis());
			}
		});
			
		Port usesPort = (Port) port.getCorbaObject();
    	UsesPortStatisticsProvider stats = UsesPortStatisticsProviderHelper.narrow(usesPort);
		while(true){
	        try{
	        	System.out.println("============================");
	        	for(UsesPortStatistics stat : stats.statistics()){
	        		System.out.println(stat.statistics.bitsPerSecond);
	        		System.out.println(new RedhawkPortStatistics(stat.statistics));
	        	}
	        }catch(Exception e){
	        	System.out.println("WELL THAT DIDN'T WORK");
	        }
	        
		}
	}
	
	@Test
	public void testProvides() throws Exception{
		ArrayBlockingQueue queue = new ArrayBlockingQueue<double[]>(7);

		/*
		 * Get Data from a live connection 
		 */
		RedhawkPort usesPort = redhawk.getDomain(domainName).getApplicationByName(applicationId).getComponentByName(componentId).getPort(portNameUses);
		final RedhawkPort sinkProvidesPort = null; // (RedhawkProvidesPortImpl) redhawk.getDomain(domainName).getApplicationByName(sinkApplicationId).getComponentByName(sinkComponentId).getPort(portNameProvides);
		usesPort.connect(new PortListener<double[]>(){

			@Override
			public void onReceive(Packet<double[]> packet) {
				//System.out.println("Receiving data");
				/*if(dataList.size()<7){
					System.out.println("Adding Data");
					dataList.add(packet);
				}*/
				rhPackage = packet;
			}
		});
		
		//RedhawkProvidesPortImpl providesPort = (RedhawkProvidesPortImpl) redhawk.getDomain(domainName).getApplicationByName(applicationId).getComponentByName(componentId).getPort(portNameProvides);
		while(true){			
			if(rhPackage!=null){
				System.out.println("Push Packet!");
				/*System.out.println("TCMode: "+rhPackage.getTime().tcmode);
				System.out.println("TCStatus: "+rhPackage.getTime().tcstatus);
				System.out.println("TOFF: "+rhPackage.getTime().toff);
				System.out.println("TWSec: "+rhPackage.getTime().twsec);
				System.out.println("TFSec: "+rhPackage.getTime().tfsec);
				System.out.println("ID: "+rhPackage.streamId);*/
				if(sinkProvidesPort!=null){
					sinkProvidesPort.send(rhPackage);
				}else
					System.out.println("Provides is null!");
			}else{
				System.out.println("Pack is null still wtf");
			}
		}
	}
	
	@Test
	public void testHearProvides() throws MultipleResourceException, Exception{
		String sriName = "helloWorld";
		
		//Initialize the uses port to pull data from 
		RedhawkPort usesPort = redhawk.getDomain(domainName).getApplicationByName(applicationId).getComponentByName(componentId).getPort(portNameUses);
		
		//Initialize the provides port to send data too 
		//RedhawkPort sinkProvidesPort = redhawk.getDomain(domainName).getApplicationByName(playApplicationId).getComponentByName(playComponentId).getPort(portNameProvides);
		final RedhawkPort sinkProvidesPort = redhawk.getDomain(domainName).getApplicationByName(this.dumpApplicationId).getComponentByName(this.dumpComponentId).getPort("dataDouble");
		
		usesPort.connect(new PortListener<double[]>(){

			@Override
			public void onReceive(Packet<double[]> packet) {
				try {
					System.out.println("Received Data");
					//System.out.println("Data Received: "+packet.getSriAsMap());
					sinkProvidesPort.send(packet);
					System.out.println("Send Data");
				} catch (Exception e) {
					System.exit(-1);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		while(true);
		
		/*int i=0;
		while(true){
			if(rhPackage!=null){
				System.out.println("Push Packet works!");
				SRIContainer sri = new SRIContainer(sriName);
				PacketContainer packet = new PacketContainer(sriName, rhPackage.getData());
				sinkProvidesPort.send(sri, packet);
				Thread.sleep(50l);
				i++; 
			}else{
				System.out.println("Pack is null still wtf");
			}
		}*/
	}
}

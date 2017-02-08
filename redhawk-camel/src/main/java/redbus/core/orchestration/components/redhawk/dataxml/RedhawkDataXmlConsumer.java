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
package redbus.core.orchestration.components.redhawk.dataxml;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import BULKIO.dataXML;
import BULKIO.dataXMLPOATie;
import redbus.core.orchestration.components.redhawk.RedhawkComponent;
import redbus.core.orchestration.components.redhawk.endpoints.RedhawkDataXmlEndpoint;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.bulkio.BulkIOData;
import redhawk.driver.bulkio.Packet;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.port.PortListener;
@Deprecated
public class RedhawkDataXmlConsumer extends DefaultConsumer {
    
    private static Log logger = LogFactory.getLog(RedhawkDataXmlConsumer.class);
    
    private dataXML pipeline;
    private RedhawkDataXmlEndpoint endpoint;
    private Processor processor;
    
    private RedhawkDeviceManager currentDeviceManager;
    
    private Thread monitorThread;
    
    public RedhawkDataXmlConsumer(RedhawkDataXmlEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.processor = processor;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        
        
        RedhawkDriver driver = ((RedhawkComponent) endpoint.getComponent()).getRedhawkDriver();
        String domainName = ((RedhawkComponent) endpoint.getComponent()).getDomainName();
        RedhawkDomainManager domainManager = driver.getDomain(domainName);        
        
        if(endpoint.getDeviceManagerName() != null){
        	currentDeviceManager = domainManager.createDeviceManager(endpoint.getDeviceManagerName(), "redbus.base", true);
        } else {
        	if(domainManager.getDeviceManagers().size() > 0){
        		currentDeviceManager = domainManager.getDeviceManagers().get(0);
        	} else {
        		currentDeviceManager = domainManager.createDeviceManager("REDBUS", "redbus.base", true);
        	}
        }        
        
        
        POA rootPOA = POAHelper.narrow(driver.getOrb().resolve_initial_references("RootPOA"));
        rootPOA.the_POAManager().activate();
        
        BulkIOData<String> data = new BulkIOData<String>(new PortListener<String>() {
			@Override
			public void onReceive(Packet<String> packet) {
		       Exchange exchange = endpoint.createExchange();
		       exchange.getIn().getHeaders().putAll(packet.getKeywords());
		       exchange.getIn().getHeaders().putAll(packet.getSriAsMap());
		       exchange.getIn().setBody(packet.getData());

		        try {
		            processor.process(exchange);
		        } catch (Exception e) {
		            logger.error(e.getMessage());
		        } finally {
		            if (exchange.getException() != null) {
		                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
		            }
		        }					
			}
		});
        
        dataXMLPOATie tie = new dataXMLPOATie(data, rootPOA);
        pipeline = tie._this(driver.getOrb());
        currentDeviceManager.getCorbaObject().registerService(pipeline, endpoint.getServiceName());
        
        monitorThread = new Thread(new Runnable() {
        	
			@Override
			public void run() {
				while(true){
					
					try {
						if(currentDeviceManager.getServiceByName(endpoint.getServiceName()) == null){
							currentDeviceManager.getCorbaObject().registerService(pipeline, endpoint.getServiceName());
						}
					} catch (Exception e){
						logger.warn("COULD NOT CONNECT TO REDHAWK");
					}
					
					try {
						Thread.currentThread().sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

        monitorThread.start();
    }

    
    @Override
    protected void doStop() throws Exception {
        super.doStop();
        monitorThread.stop();
        try {
        	currentDeviceManager.getCorbaObject().unregisterService(pipeline, endpoint.getServiceName());
        } catch (Exception e) {
            logger.error("Could Not Unregister Service: " + endpoint.getServiceName() + " with the REDHAWK DEVICE MANAGER. " + e.getMessage());
        }
    }

    
}
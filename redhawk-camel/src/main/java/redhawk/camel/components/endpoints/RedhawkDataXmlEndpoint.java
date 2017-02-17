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
package redhawk.camel.components.endpoints;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redhawk.camel.components.RedhawkComponent;
import redhawk.camel.components.dataxml.RedhawkDataXmlConsumer;
import redhawk.camel.components.dataxml.RedhawkDataXmlProducer;

@Deprecated
public class RedhawkDataXmlEndpoint extends DefaultEndpoint {

    private static Log logger = LogFactory.getLog(RedhawkDataXmlEndpoint.class);

    private String serviceName;
    private String deviceManagerName;
    
    private RedhawkDataXmlProducer producer;
    private RedhawkDataXmlConsumer consumer;
    
    public RedhawkDataXmlEndpoint(String uri, RedhawkComponent component){
    	super(uri, component);
    }
    
    	
    public Producer createProducer() throws Exception {
        if(producer == null){
            producer = new RedhawkDataXmlProducer(this);
        }
        
        return producer; 
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        if(consumer == null){
            consumer = new RedhawkDataXmlConsumer(this, processor);
        }
        
        return consumer;
    }

    public boolean isSingleton() {
        return true;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        
        if(producer != null && producer.isStopped()){
            producer.start();
        }
        
        if(consumer != null && consumer.isStopped()){
            consumer.start();
        }
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        
        if(producer != null && producer.isStarted()){
            producer.stop();
        }
        
        if(consumer != null && consumer.isStarted()){
            consumer.stop();
        }
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public String getDeviceManagerName() {
        return deviceManagerName;
    }

    public void setDeviceManagerName(String deviceManagerName) {
        this.deviceManagerName = deviceManagerName;
    }

}
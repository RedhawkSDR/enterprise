/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package redbus.core.orchestration.components.redhawk.endpoints;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redbus.core.orchestration.components.redhawk.RedhawkComponent;
import redbus.core.orchestration.components.redhawk.dataxml.RedhawkDataXmlConsumer;
import redbus.core.orchestration.components.redhawk.dataxml.RedhawkDataXmlProducer;

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
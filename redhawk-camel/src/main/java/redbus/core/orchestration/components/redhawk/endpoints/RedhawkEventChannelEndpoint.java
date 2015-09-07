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
import redbus.core.orchestration.components.redhawk.eventchannel.RedhawkEventChannelConsumer;
import redbus.core.orchestration.components.redhawk.eventchannel.RedhawkEventChannelProducer;

public class RedhawkEventChannelEndpoint extends DefaultEndpoint {

    private static Log logger = LogFactory.getLog(RedhawkEventChannelEndpoint.class);

    private String eventChannelName;
    private String dataTypeName;
    private boolean convertAnysToMaps = true;
    
    
    private RedhawkEventChannelConsumer consumer;
    private RedhawkEventChannelProducer producer;

    public RedhawkEventChannelEndpoint(String uri, RedhawkComponent component) {
        super(uri, component);
    }

    public RedhawkEventChannelEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        if (producer == null) {
            producer = new RedhawkEventChannelProducer(this);
        }

        return producer;
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        if (consumer == null) {
            consumer = new RedhawkEventChannelConsumer(this, processor);
        }

        return consumer;
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
    
    public boolean isSingleton() {
        return true;
    }

    public String getEventChannelName() {
        return eventChannelName;
    }

    public void setEventChannelName(String eventChannelName) {
        this.eventChannelName = eventChannelName;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }

    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }

	public boolean isConvertAnysToMaps() {
		return convertAnysToMaps;
	}

	public void setConvertAnysToMaps(boolean convertAnysToMaps) {
		this.convertAnysToMaps = convertAnysToMaps;
	}

}

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

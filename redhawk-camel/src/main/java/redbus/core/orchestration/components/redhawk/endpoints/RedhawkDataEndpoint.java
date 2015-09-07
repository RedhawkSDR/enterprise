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
import redbus.core.orchestration.components.redhawk.data.RedhawkDataConsumer;

public class RedhawkDataEndpoint extends DefaultEndpoint {

	private static Log logger = LogFactory.getLog(RedhawkDataEndpoint.class);

	private int bufferSize = 2000;
	private String componentName;
	private RedhawkDataConsumer consumer;
	private int dbOffset = 0;
	private int decimateTo = -1;
	private String portName;
	private String portType;

	private String waveformName;

	public RedhawkDataEndpoint(String uri, RedhawkComponent component) {
		super(uri, component);
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		if (consumer == null) {
			consumer = new RedhawkDataConsumer(this, processor);
		}

		return consumer;
	}

	public Producer createProducer() throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void doStart() throws Exception {
		super.doStart();

		if (consumer != null && consumer.isStopped()) {
			consumer.start();
		}
	}

	@Override
	protected void doStop() throws Exception {
		super.doStop();

		if (consumer != null && consumer.isStarted()) {
			consumer.stop();
		}
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public String getComponentName() {
		return componentName;
	}

	public int getDbOffset() {
		return dbOffset;
	}

	public int getDecimateTo() {
		return decimateTo;
	}

	public String getPortName() {
		return portName;
	}

	public String getPortType() {
		return portType;
	}

	public String getWaveformName() {
		return waveformName;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public void setDbOffset(int dbOffset) {
		this.dbOffset = dbOffset;
	}

	public void setDecimateTo(int decimateTo) {
		this.decimateTo = decimateTo;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public void setPortType(String portType) {
		this.portType = portType;
	}

	public void setWaveformName(String waveformName) {
		this.waveformName = waveformName;
	}

}
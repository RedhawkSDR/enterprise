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
import redhawk.camel.components.data.RedhawkDataConsumer;

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
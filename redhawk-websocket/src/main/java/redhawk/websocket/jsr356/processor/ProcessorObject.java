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
package redhawk.websocket.jsr356.processor;

import java.util.Map;

/**
 * POJO to represent configuration of a WebSocketProcessor
 */
public class ProcessorObject {
	/**
	 * Name of the processor
	 */
	String processorName;
	
	/**
	 * Map of the processor configurations.
	 */
	Map<String, String> processorConfiguration;	
	
	/**
	 * Returns the name of a WebSocketProcessor
	 * @return
	 * 		Returns the name of the processor
	 */
	public String getProcessorName() {
		return processorName;
	}

	/**
	 * Sets the name of a WebSocketProcessor
	 * @param processorName
	 * 		Sets the name of the processor.
	 */
	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}

	/**
	 * Returns the configuration of a WebSocketProcessor
	 * @return
	 * 		Returns the key-value pair related to the processor configuration.
	 */
	public Map<String, String> getProcessorConfiguration() {
		return processorConfiguration;
	}

	/**
	 * Sets the configuation of a WebSocketProcessor. 
	 * @param processorConfiguration
	 * 		Set the processor configuration. 
	 */
	public void setProcessorConfiguration(Map<String, String> processorConfiguration) {
		this.processorConfiguration = processorConfiguration;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessorObject other = (ProcessorObject) obj;
		if (processorName == null && (other.processorName!=null)) {
				return false;
		} else if (!processorName.equals(other.processorName))
			return false;
		return true;
	}
	
}

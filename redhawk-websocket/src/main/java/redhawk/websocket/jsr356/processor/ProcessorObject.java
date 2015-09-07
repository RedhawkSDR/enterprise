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

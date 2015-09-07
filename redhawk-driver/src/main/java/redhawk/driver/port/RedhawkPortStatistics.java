package redhawk.driver.port;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import BULKIO.PortStatistics;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RedhawkPortStatistics {
	private String portName; 
	
	private float elementsPerSecond; 
	
	private float bitsPerSecond; 
	
	private float callsPerSecond; 
	
	private List<String> streamIDs = new ArrayList<>();
	
	private float timeSinceLastCall; 
	
	private Object[] keyWords; 
	
	private float averageQueueDepth;
	
	public RedhawkPortStatistics(){	
	}
	
	public RedhawkPortStatistics(PortStatistics stats){
		Collections.addAll(streamIDs, stats.streamIDs);		
		this.setPortName(stats.portName);
		this.setBitsPerSecond(stats.bitsPerSecond);
		this.setCallsPerSecond(stats.callsPerSecond); 
		this.setTimeSinceLastCall(stats.timeSinceLastCall);
		this.setElementsPerSecont(stats.elementsPerSecond);
	}
	
	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public float getElementsPerSecont() {
		return elementsPerSecond;
	}

	public void setElementsPerSecont(float elementsPerSecont) {
		this.elementsPerSecond = elementsPerSecont;
	}

	public float getBitsPerSecond() {
		return bitsPerSecond;
	}

	public void setBitsPerSecond(float bitsPerSecond) {
		this.bitsPerSecond = bitsPerSecond;
	}

	public float getCallsPerSecond() {
		return callsPerSecond;
	}

	public void setCallsPerSecond(float callsPerSecond) {
		this.callsPerSecond = callsPerSecond;
	}

	public List<String> getStreamIDs() {
		return streamIDs;
	}

	public void setStreamIDs(List<String> streamIDs) {
		this.streamIDs = streamIDs;
	}

	public float getTimeSinceLastCall() {
		return timeSinceLastCall;
	}

	public void setTimeSinceLastCall(float timeSinceLastCall) {
		this.timeSinceLastCall = timeSinceLastCall;
	}

	public float getAverageQueueDepth() {
		return averageQueueDepth;
	}

	public void setAverageQueueDepth(float averageQueueDepth) {
		this.averageQueueDepth = averageQueueDepth;
	}
}

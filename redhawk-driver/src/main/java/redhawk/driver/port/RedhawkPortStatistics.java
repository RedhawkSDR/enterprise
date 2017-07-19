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
package redhawk.driver.port;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import BULKIO.PortStatistics;

/**
 * XML for PortStatistics object. 
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RedhawkPortStatistics {
	private String connectionId; 
	
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
	
	public RedhawkPortStatistics(String connectionId, PortStatistics stats){
		this(stats);
		this.connectionId = connectionId;
	}
	
	public RedhawkPortStatistics(PortStatistics stats){
		Collections.addAll(streamIDs, stats.streamIDs);		
		this.setPortName(stats.portName);
		this.setBitsPerSecond(stats.bitsPerSecond);
		this.setCallsPerSecond(stats.callsPerSecond); 
		this.setTimeSinceLastCall(stats.timeSinceLastCall);
		this.setElementsPerSecond(stats.elementsPerSecond);
		this.setKeyWords(stats.keywords);
	}
	
	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public float getElementsPerSecond() {
		return elementsPerSecond;
	}

	public void setElementsPerSecond(float elementsPerSecond) {
		this.elementsPerSecond = elementsPerSecond;
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

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

	public Object[] getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(Object[] keyWords) {
		this.keyWords = keyWords;
	}

	@Override
	public String toString() {
		return "RedhawkPortStatistics [connectionId=" + connectionId + ", portName=" + portName + ", elementsPerSecond="
				+ elementsPerSecond + ", bitsPerSecond=" + bitsPerSecond + ", callsPerSecond=" + callsPerSecond
				+ ", streamIDs=" + streamIDs + ", timeSinceLastCall=" + timeSinceLastCall + ", keyWords="
				+ Arrays.toString(keyWords) + ", averageQueueDepth=" + averageQueueDepth + "]";
	}
}

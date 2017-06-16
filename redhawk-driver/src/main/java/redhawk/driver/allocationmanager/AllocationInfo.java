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
package redhawk.driver.allocationmanager;

import java.util.Map;

public class AllocationInfo {
	private String allocationId;
	
	private String requestingDomain; 
	
	private Map<String, Object> allocationProperties;
	
	private String allocatedDeviceId;; 
	
	private String deviceManagerId;
	
	private String sourceId;
	
	public AllocationInfo(){}

	public String getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}

	public String getRequestingDomain() {
		return requestingDomain;
	}

	public void setRequestingDomain(String requestingDomain) {
		this.requestingDomain = requestingDomain;
	}

	public Map<String, Object> getAllocationProperties() {
		return allocationProperties;
	}

	public void setAllocationProperties(Map<String, Object> allocationProperties) {
		this.allocationProperties = allocationProperties;
	}

	public String getAllocatedDeviceId() {
		return allocatedDeviceId;
	}

	public void setAllocatedDeviceId(String allocatedDeviceId) {
		this.allocatedDeviceId = allocatedDeviceId;
	}

	public String getDeviceManagerId() {
		return deviceManagerId;
	}

	public void setDeviceManagerId(String deviceManagerId) {
		this.deviceManagerId = deviceManagerId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Override
	public String toString() {
		return "AllocationInfo [allocationId=" + allocationId + ", requestingDomain=" + requestingDomain
				+ ", allocationProperties=" + allocationProperties + ", allocatedDeviceId=" + allocatedDeviceId
				+ ", deviceManagerId=" + deviceManagerId + ", sourceId=" + sourceId + "]";
	}
	
	
}

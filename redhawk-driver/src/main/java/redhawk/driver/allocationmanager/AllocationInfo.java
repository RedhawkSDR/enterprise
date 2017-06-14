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

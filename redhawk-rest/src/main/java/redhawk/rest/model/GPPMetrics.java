package redhawk.rest.model;

import java.util.List;
import java.util.Map;

public class GPPMetrics extends MetricsBase {
	private String device; 

	private List<Map<String, Object>> component_monitor;
	
	//TODO: Might need to make this a list
	private Map<String, Object> utilization;
	
	private Map<String, Object> sys_limits;
	
	List<Map<String, Object>> nic_metrics;
	
	//TODO: Get load average

	public GPPMetrics() {
		
	}

	public List<Map<String, Object>> getComponent_monitor() {
		return component_monitor;
	}

	public void setComponent_monitor(List<Map<String, Object>> component_monitor) {
		this.component_monitor = component_monitor;
	}

	public Map<String, Object> getUtilization() {
		return utilization;
	}

	public void setUtilization(Map<String, Object> utilization) {
		this.utilization = utilization;
	}

	public Map<String, Object> getSys_limits() {
		return sys_limits;
	}

	public void setSys_limits(Map<String, Object> sys_limits) {
		this.sys_limits = sys_limits;
	}

	public List<Map<String, Object>> getNic_metrics() {
		return nic_metrics;
	}

	public void setNic_metrics(List<Map<String, Object>> nic_metrics) {
		this.nic_metrics = nic_metrics;
	}	
	
	public String getDevice() {
		return device;
	}

	public void setDevice(String deviceName) {
		this.device = deviceName;
	}

	@Override
	public String toString() {
		return "GPPMetrics [deviceName=" + device + ", component_monitor=" + component_monitor + ", utilization="
				+ utilization + ", sys_limits=" + sys_limits + ", nic_metrics=" + nic_metrics + "]";
	}
}

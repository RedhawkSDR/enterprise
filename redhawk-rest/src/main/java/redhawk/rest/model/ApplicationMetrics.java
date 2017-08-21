package redhawk.rest.model;

import java.util.Map;

public class ApplicationMetrics {
	private String appName; 
	
	private Map<String, Map<String, Object>> metrics;
	
	public ApplicationMetrics(String appName, Map<String, Map<String, Object>> metrics) {
		this.appName = appName; 
		this.metrics = metrics;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Map<String, Map<String, Object>> getMetrics() {
		return metrics;
	}

	public void setMetrics(Map<String, Map<String, Object>> metrics) {
		this.metrics = metrics;
	}
}

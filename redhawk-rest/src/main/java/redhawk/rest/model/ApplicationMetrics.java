package redhawk.rest.model;

import java.util.List;
import java.util.Map;

public class ApplicationMetrics {
	private String application; 
	
	private List<Map<String, Object>> metrics; 
	
	
	public ApplicationMetrics(String application, List<Map<String, Object>> metrics) {
		this.application = application; 
		this.metrics = metrics;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String appName) {
		this.application = appName;
	}

	public List<Map<String, Object>> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<Map<String, Object>> metrics) {
		this.metrics = metrics;
	}
}

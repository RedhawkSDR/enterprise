package redhawk.rest.model;

import java.util.List;

import redhawk.driver.port.RedhawkPortStatistics;

public class PortMetrics {
	private String appName; 
	
	private String componentName; 
	
	private List<RedhawkPortStatistics> statistics;

	public PortMetrics(String appName, String componentName, List<RedhawkPortStatistics> statistics) {
		super();
		this.appName = appName;
		this.componentName = componentName;
		this.statistics = statistics;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public List<RedhawkPortStatistics> getStatistics() {
		return statistics;
	}

	public void setStatistics(List<RedhawkPortStatistics> statistics) {
		this.statistics = statistics;
	}
}

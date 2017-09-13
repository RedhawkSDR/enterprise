package redhawk.rest.model;

import java.util.List;

import redhawk.driver.port.RedhawkPortStatistics;

public class PortMetrics {
	private String application; 
	
	private String component; 
	
	private String port;

	private List<RedhawkPortStatistics> statistics;

	public PortMetrics(String appName, String componentName, String port, List<RedhawkPortStatistics> statistics) {
		super();
		this.application = appName;
		this.component = componentName;
		this.port = port;
		this.statistics = statistics;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String appName) {
		this.application = appName;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String componentName) {
		this.component = componentName;
	}
	
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public List<RedhawkPortStatistics> getStatistics() {
		return statistics;
	}

	public void setStatistics(List<RedhawkPortStatistics> statistics) {
		this.statistics = statistics;
	}
}

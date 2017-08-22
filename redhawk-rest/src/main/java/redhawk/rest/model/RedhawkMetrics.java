package redhawk.rest.model;

import java.util.List;

public class RedhawkMetrics {
	private List<ApplicationMetrics> applicationMetrics;
	
	private List<PortMetrics> portStatistics;
	
	private List<GPPMetrics> gppMetrics; 

	public RedhawkMetrics() {
		
	}
	
	public RedhawkMetrics(List<ApplicationMetrics> applicationMetrics, List<PortMetrics> portStatistics) {
		super();
		this.applicationMetrics = applicationMetrics;
		this.portStatistics = portStatistics;
	}

	public List<ApplicationMetrics> getApplicationMetrics() {
		return applicationMetrics;
	}

	public void setApplicationMetrics(List<ApplicationMetrics> applicationMetrics) {
		this.applicationMetrics = applicationMetrics;
	}

	public List<PortMetrics> getPortStatistics() {
		return portStatistics;
	}

	public void setPortStatistics(List<PortMetrics> portStatistics) {
		this.portStatistics = portStatistics;
	}

	public List<GPPMetrics> getGppMetrics() {
		return gppMetrics;
	}

	public void setGppMetrics(List<GPPMetrics> gppMetrics) {
		this.gppMetrics = gppMetrics;
	} 
}

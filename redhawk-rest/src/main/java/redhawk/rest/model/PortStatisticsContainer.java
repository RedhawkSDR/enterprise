package redhawk.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import redhawk.driver.port.RedhawkPortStatistics;

@XmlRootElement(name="statistics")
@XmlAccessorType(XmlAccessType.FIELD)
public class PortStatisticsContainer {
	@XmlElementWrapper(name="statistics")
	@XmlElement(name="statistic")
	private List<RedhawkPortStatistics> statistics; 
	
	public PortStatisticsContainer(){
	}
	
	public PortStatisticsContainer(List<RedhawkPortStatistics> statistics){
		this.setStatistics(statistics);
	}

	public List<RedhawkPortStatistics> getStatistics() {
		return statistics;
	}

	public void setStatistics(List<RedhawkPortStatistics> statistics) {
		this.statistics = statistics;
	}
}

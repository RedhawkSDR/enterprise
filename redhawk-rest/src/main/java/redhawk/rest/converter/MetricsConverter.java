package redhawk.rest.converter;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import redhawk.driver.Redhawk;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.port.RedhawkPortStatistics;
import redhawk.rest.RedhawkManager;
import redhawk.rest.model.ApplicationMetrics;
import redhawk.rest.model.PortMetrics;
import redhawk.rest.model.RedhawkMetrics;
import redhawk.rest.utils.MetricTypes;

/**
 * Utility class for converting rest metric calls to Responses for client
 */
public class MetricsConverter {
	public static RedhawkMetrics getMetrics(RedhawkManager manager, String nameServer, String domainName) throws WebApplicationException{
		Redhawk driver = null; 
		RedhawkMetrics metrics = new RedhawkMetrics();

		try {
			driver = manager.getDriverInstance(nameServer);
			RedhawkDomainManager domain = driver.getDomain(domainName);

			/*
			 * Loop through specifies types and give back answer
			 */
			for (MetricTypes type : MetricTypes.values()) {
				switch (type) {
				case PORT:
					List<PortMetrics> pMetrics = convertPortMetrics(domain.getApplications());
					metrics.setPortStatistics(pMetrics);
					break;
				case APPLICATION:
					metrics.setApplicationMetrics(convertApplicationMetrics(domain.getApplications()));
					break;
				}
			}
		} catch (Exception ex) {
			throw new WebApplicationException(ex);
		}finally {
			if(driver!=null)
				driver.disconnect();
		}

		return metrics;
	}

	public static <T> T getMetricByType(RedhawkManager manager, String nameServer, String domainName, MetricTypes type){
		Redhawk driver = null; 

		try {
			driver = manager.getDriverInstance(nameServer);
			RedhawkDomainManager domain = driver.getDomain(domainName);
			
			switch(type) {
			case APPLICATION: 
				//Get the Application Metrics
				List<ApplicationMetrics> appMetrics = convertApplicationMetrics(domain.getApplications());
				return (T) appMetrics;
			case PORT:
				//Get the Port Metrics
				List<PortMetrics> portMetrics = convertPortMetrics(domain.getApplications());
				return (T) portMetrics;
			default:
				//TODO: 
				return null;
			}
		}catch(Exception ex) {
			throw new WebApplicationException(ex);
		}finally {
			if(driver!=null)
				driver.disconnect();
		}
	}

	/**
	 * Helper method to convert all application metrics into the appropriate
	 * response object
	 * 
	 * @param applications
	 * @return
	 * @throws WebApplicationException
	 */
	public static List<ApplicationMetrics> convertApplicationMetrics(List<RedhawkApplication> applications)
			throws WebApplicationException {
		/*
		 * Loop over each application and get it's metrics
		 */
		List<ApplicationMetrics> metrics = new ArrayList<>();

		for (RedhawkApplication app : applications) {
			String appName = app.getName();
			try {
				metrics.add(new ApplicationMetrics(appName, app.getMetrics()));
			} catch (ApplicationException e) {
				throw new WebApplicationException(e);
			}
		}

		return metrics;
	}

	/**
	 * Helper method to convert all port metrics into the appropriate 
	 * response object
	 * 
	 * @param applications
	 * @return
	 * @throws WebApplicationException
	 */
	public static List<PortMetrics> convertPortMetrics(List<RedhawkApplication> applications)
			throws WebApplicationException {
		/*
		 * Loop over applications and get their components then from their you can get
		 * each applications metrics
		 */
		List<PortMetrics> metrics = new ArrayList<>();

		for (RedhawkApplication app : applications) {
			// Get an apps components
			String appName = app.getName();
			for (RedhawkComponent comp : app.getComponents()) {
				String componentName = comp.getName();

				// Loop over a components ports
				try {
					for (RedhawkPort port : comp.getPorts()) {
						List<RedhawkPortStatistics> stats = port.getPortStatistics();
						metrics.add(new PortMetrics(appName, componentName, stats));
					}
				} catch (ResourceNotFoundException e) {
					throw new WebApplicationException(e);
				}
			}
		}

		return metrics;
	}
}

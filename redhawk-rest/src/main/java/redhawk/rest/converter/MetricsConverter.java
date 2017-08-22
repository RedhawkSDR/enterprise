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
package redhawk.rest.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.WebApplicationException;

import org.apache.log4j.Logger;

import redhawk.driver.Redhawk;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.port.RedhawkPortStatistics;
import redhawk.driver.properties.RedhawkProperty;
import redhawk.driver.properties.RedhawkStruct;
import redhawk.driver.properties.RedhawkStructSequence;
import redhawk.rest.RedhawkManager;
import redhawk.rest.model.ApplicationMetrics;
import redhawk.rest.model.GPPMetrics;
import redhawk.rest.model.PortMetrics;
import redhawk.rest.model.RedhawkMetrics;
import redhawk.rest.utils.MetricTypes;

/**
 * Utility class for converting rest metric calls to Responses for client
 */
public class MetricsConverter {
	static Logger logger = Logger.getLogger(MetricsConverter.class);
	
	public static RedhawkMetrics getMetrics(RedhawkManager manager, String nameServer, String domainName)
			throws WebApplicationException {
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
				case GPP:
					metrics.setGppMetrics(convertGPPMetrics(domain.getDevices()));
					break;
				}
			}
		} catch (Exception ex) {
			throw new WebApplicationException(ex);
		} finally {
			if (driver != null)
				driver.disconnect();
		}

		return metrics;
	}

	/**
	 * Returns metrics based upon the type you pass in.
	 * 
	 * @param manager
	 * @param nameServer
	 * @param domainName
	 * @param type
	 * @return
	 */
	public static <T> T getMetricByType(RedhawkManager manager, String nameServer, String domainName,
			MetricTypes type) {
		Redhawk driver = null;

		try {
			driver = manager.getDriverInstance(nameServer);
			RedhawkDomainManager domain = driver.getDomain(domainName);

			switch (type) {
			case APPLICATION:
				// Get the Application Metrics
				List<ApplicationMetrics> appMetrics = convertApplicationMetrics(domain.getApplications());
				return (T) appMetrics;
			case PORT:
				// Get the Port Metrics
				List<PortMetrics> portMetrics = convertPortMetrics(domain.getApplications());
				return (T) portMetrics;
			case GPP:
				Collection<RedhawkDevice> devices = domain.devices().values();
				return (T) convertGPPMetrics(devices);
			default:
				throw new WebApplicationException("Unhandled Metric Type");
			}
		} catch (Exception ex) {
			throw new WebApplicationException(ex);
		} finally {
			if (driver != null)
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
	 * Helper method to convert all port metrics into the appropriate response
	 * object
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
	
	/**
	 * Convert GPP Metrics from Device list
	 * @param devices
	 * @return
	 */
	public static List<GPPMetrics> convertGPPMetrics(Collection<RedhawkDevice> devices) {
		List<GPPMetrics> metrics = new ArrayList<>();
		String[] metricKeys = new String[] { "component_monitor", "utilization", "sys_limits", "nic_metrics" };

		/*
		 * Loop through devices and ones that are GPP
		 */
		for (RedhawkDevice device : devices) {
			String deviceKind = device.getProperty("device_kind");
			GPPMetrics gppMetric = new GPPMetrics();
			
			/*
			 * Only doing this for GPP
			 */
			if (deviceKind!=null && deviceKind.equals("GPP")) {
				
				/*
				 * Loop over the accepted keys and add to response
				 */
				for (String metricKey : metricKeys) {
					RedhawkProperty property = device.getProperties().get(metricKey);
					if (property instanceof RedhawkStructSequence) {
						RedhawkStructSequence propToConvert = (RedhawkStructSequence) property;

						switch (metricKey) {
						case "component_monitor":
							String stripBeginning = "component_monitor::component_monitor::";
							List<Map<String, Object>> cmMetrics = structToNormalizedCollection(stripBeginning,
									propToConvert.toListOfMaps());

							gppMetric.setComponent_monitor(cmMetrics);
							break;
						case "utilization":
							for (Map<String, Object> componentMonitor : propToConvert.toListOfMaps()) {
								// TODO: This might need to be a list
								gppMetric.setUtilization(componentMonitor);
								break;
							}
							break;
						case "nic_metrics":
							String nicStrip = "nic_metrics::";
							List<Map<String, Object>> normalizedNicMetrics = structToNormalizedCollection(nicStrip,
									propToConvert.toListOfMaps());
							gppMetric.setNic_metrics(normalizedNicMetrics);
							break;
						default:
							System.err.println("Unhandled Key: " + metricKey);
						}
					} else if (property instanceof RedhawkStruct) {
						RedhawkStruct struct = (RedhawkStruct) property;
						String sysLimitStrip = "sys_limits::";
						Map<String, Object> sysLimit = new HashMap<>();

						for (Entry<String, Object> sEntry : struct.entrySet()) {
							String key = sEntry.getKey().replaceAll(sysLimitStrip, "");
							sysLimit.put(key, sEntry.getValue());
						}

						gppMetric.setSys_limits(sysLimit);
					} else {
						logger.error("Unhandled tpye");
					}
					
					metrics.add(gppMetric);
				}
			}
		}
		
		return metrics;
	}

	/**
	 * Clean up unnecessary padding on key values
	 * 
	 * @return
	 */
	private static List<Map<String, Object>> structToNormalizedCollection(String strip,
			List<Map<String, Object>> structAttributes) {
		List<Map<String, Object>> normalizedStructMetrics = new ArrayList<>();

		/*
		 * Loop through component_monitor entries
		 */
		for (Map<String, Object> structAttribute : structAttributes) {
			/*
			 * Remove unncessary key padding
			 */
			Map<String, Object> normalizedStructEntry = new HashMap<>();
			for (Map.Entry<String, Object> cmEntry : structAttribute.entrySet()) {
				String key = cmEntry.getKey().replaceAll(strip, "");
				normalizedStructEntry.put(key, cmEntry.getValue());
			}

			// Add to list
			normalizedStructMetrics.add(normalizedStructEntry);
		}

		return normalizedStructMetrics;
	}
}

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
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.port.RedhawkPortStatistics;
import redhawk.driver.properties.RedhawkProperty;
import redhawk.driver.properties.RedhawkSimple;
import redhawk.driver.properties.RedhawkStruct;
import redhawk.driver.properties.RedhawkStructSequence;
import redhawk.rest.RedhawkManager;
import redhawk.rest.model.ApplicationMetrics;
import redhawk.rest.model.GPPMetrics;
import redhawk.rest.model.MetricFilter;
import redhawk.rest.model.PortMetrics;
import redhawk.rest.model.RedhawkMetrics;
import redhawk.rest.utils.MetricTypes;

/**
 * Utility class for converting rest metric calls to Responses for client
 */
public class MetricsConverter {
	static Logger logger = Logger.getLogger(MetricsConverter.class);
	
	/**
	 * Returns metrics of all types(APP, GPP, PORT)
	 * 
	 * @param manager
	 * @param nameServer
	 * @param domainName
	 * @return
	 * @throws WebApplicationException
	 */
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
	 * Returns metrics based on filter param. This can be a 
	 * @param manager
	 * @param nameServer
	 * @param domainName
	 * @param filter
	 * @return
	 */
	public static <T> T getMetricsByTypeAndFilter(RedhawkManager manager, String nameServer, String domainName, MetricTypes type, String filter) {
		Redhawk driver = null;

		try {
			driver = manager.getDriverInstance(nameServer);
			RedhawkDomainManager domain = driver.getDomain(domainName);
		
			switch (type) {
			case APPLICATION:
				// Get the Application Metrics
				List<ApplicationMetrics> appMetrics = convertApplicationMetrics(domain.getApplicationsByName(filter));
				return (T) appMetrics;
			case GPP:
				Collection<RedhawkDevice> devices = domain.getDevicesByName(filter);
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
	
	public static <T> T getAppMetricsByMetricType(RedhawkManager manager, String nameServer, String domainName, String applicationName, MetricFilter filter) {
		Redhawk driver = null;

		try {
			driver = manager.getDriverInstance(nameServer);
			RedhawkDomainManager domain = driver.getDomain(domainName);
			
			return (T) domain.getApplicationByName(applicationName).getMetrics(filter.getComponents(), filter.getAttributes());
		}catch(Exception ex) {
			throw new WebApplicationException(ex);
		}finally {
			if(driver!=null)
				driver.disconnect();
		}
	}
	
	/**
	 * Returns all metrics based upon the type you pass in.
	 * 
	 * @param manager
	 * @param nameServer
	 * @param domainName
	 * @param type
	 * @return
	 */
	public static <T> T getMetricsByType(RedhawkManager manager, String nameServer, String domainName,
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
	
	public static <T> T getAvailableMetrics(RedhawkManager manager, String nameServer, String domainName) {
		return getAvailableMetrics(manager, nameServer, domainName, false);
	}
	
	public static <T> T getAvailableMetrics(RedhawkManager manager, String nameServer, String domainName, Boolean treeFormat) {
		Redhawk driver = null;

		try {
			driver = manager.getDriverInstance(nameServer);
			RedhawkDomainManager domain = driver.getDomain(domainName);
			Map<String, Object> json = new HashMap<>();
			List<RedhawkApplication> applications = null; 
			
			for (MetricTypes type : MetricTypes.values()) {
				switch (type) {
				case APPLICATION:
					if(applications==null)
						applications = domain.getApplications();
					
					List<String> applicationNames = new ArrayList<>();
					for (RedhawkApplication app : applications) {
						applicationNames.add(app.getName());
					}
					json.put(MetricTypes.APPLICATION.toString(), applicationNames);
					break;
				case GPP:
					//TODO: Should go by device kind
					List<RedhawkDevice> devices = domain.getDevicesByName("GPP.*");
					List<String> names = new ArrayList<>();
					
					for(RedhawkDevice dev : devices) {
						names.add(dev.getName());
					}
					
					json.put(MetricTypes.GPP.toString(), names);
					break;
				case PORT:					
					if(applications==null)
						applications = domain.getApplications();
					
					Object ports;
					if(treeFormat) {
						ports = MetricsConverterUtils.getPortsAvailableAsTree(applications);
					}else {
						ports = MetricsConverterUtils.getPortsAvailable(applications);
					}	
					
					json.put(MetricTypes.PORT.toString(), ports);
					break;
				default:
					logger.error("Unhandled type "+type);
				}
			}
			
			return (T) json;
		}catch(Exception ex) {
			throw new WebApplicationException(ex);
		}finally {
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
				/*
				 * Putting metrics into a list instead of a Map because follow on systems 
				 * will have an easier time iterating over JSON without dynamic keys that
				 * come back from the driver. Flattened structure should be easier to handle. 
				 */
				List<Map<String, Object>> metricsList = new ArrayList<>();
				
				for(Map.Entry<String, Map<String, Object>> entry : app.getMetrics().entrySet()) {
					Map<String, Object> obj = entry.getValue();
					obj.put("metricId", entry.getKey());
					
					metricsList.add(obj);
				}
				
				metrics.add(new ApplicationMetrics(appName, metricsList));
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
						try {
							List<RedhawkPortStatistics> stats = port.getPortStatistics();
							if(!stats.isEmpty())
								metrics.add(new PortMetrics(appName, componentName, port.getName(), stats));
							else 
								logger.debug("Not reporting stats for port with empty statistics "+port.getName());
						}catch(Exception ex) {
							logger.error("Unable to query port "+port.getName());
						}
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
			RedhawkSimple deviceKind = device.getProperty("device_kind");
			GPPMetrics gppMetric = new GPPMetrics();
			
			/*
			 * Only doing this for GPP
			 */
			//TODO: second logic is a hack to get around not seeing device kind when running with maven
			if ((deviceKind!=null && deviceKind.getValue().equals("GPP")) || device.getName().contains("GPP")) {
				System.out.println("Made it in if");
				//Set name
				gppMetric.setDevice(device.getName());
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
						Map<String, Object> value = struct.getValue();
						for (Entry<String, Object> sEntry : value.entrySet()) {
							String key = sEntry.getKey().replaceAll(sysLimitStrip, "");
							sysLimit.put(key, sEntry.getValue());
						}

						gppMetric.setSys_limits(sysLimit);
					} else {
						logger.error("Unhandled tpye");
					}					
				}
				
				//Add metric
				metrics.add(gppMetric);
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

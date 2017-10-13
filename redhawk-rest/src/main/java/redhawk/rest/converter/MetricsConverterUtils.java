package redhawk.rest.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;

public class MetricsConverterUtils {
	private static Logger logger = LoggerFactory.getLogger(MetricsConverterUtils.class);

	public static List<Map<String, String>> getPortsAvailable(List<RedhawkApplication> applications) {
		List<Map<String, String>> ports = new ArrayList<>();
		
		for (RedhawkApplication application : applications) {
			String appName = application.getName();

			for (RedhawkComponent comp : application.getComponents()) {
				String component = comp.getName();

				try {
					List<String> portNames = new ArrayList<>();
					for (RedhawkPort port : comp.getPorts()) {
						try {
							if (!port.getPortStatistics().isEmpty()) {
								// Create map and add to list
								Map<String, String> map = new HashMap<>();
								map.put("APP", appName);
								map.put("COMPONENT", component);
								map.put("PORT", port.getName());
								
								ports.add(map);
							}
						} catch (Exception ex) {
							logger.error("Exception trying to get port stats from " + port.getName(), ex);
						}
					}
				} catch (ResourceNotFoundException e) {
					logger.error("Unable to query ports", e);
				}
			}
		}
		
		return ports;
	}

	/**
	 * Not sure we'll ever use this but this is algorithm for getting tree format
	 * from Ports
	 * 
	 * @param applications
	 * @return
	 */
	public static Map<String, Map<String, Map<String, Map<String, Map<String, List<String>>>>>> getPortsAvailableAsTree(
			List<RedhawkApplication> applications) {
		/*
		 * Time to create a tree PORT +-APPLICATION +--<APP NAME> +---COMPONENTS
		 * +----<COMPONENT NAME> +-----PORTS +------<PORT NAMES>
		 */
		Map<String, Map<String, Map<String, Map<String, Map<String, List<String>>>>>> portTree = new HashMap<>();

		for (RedhawkApplication application : applications) {
			String appName = application.getName();

			/*
			 * COMPONENTS +-<Component Name> +--PORTS +---<PORT Name>
			 */
			Map<String, Map<String, Map<String, List<String>>>> appComponents = new HashMap<>();
			// Loop through components
			for (RedhawkComponent comp : application.getComponents()) {
				String componentName = comp.getName();

				// Loop through ports and add names for ports that have stats
				List<RedhawkPort> ports;
				try {
					ports = comp.getPorts();

					List<String> portNames = new ArrayList<>();
					for (RedhawkPort port : ports) {
						try {
							if (!port.getPortStatistics().isEmpty())
								portNames.add(port.getName());
						} catch (Exception ex) {
							logger.error("Exception trying to get port stats from " + port.getName(), ex);
						}
					}

					// Create port map
					Map<String, List<String>> portMap = new HashMap<>();
					portMap.put("PORTS", portNames);

					// Hold component to port map
					Map<String, Map<String, List<String>>> compPortMap;
					// Update component Map
					if (appComponents.containsKey("COMPONENTS")) {
						// Retrive current components port map and update
						compPortMap = (Map<String, Map<String, List<String>>>) appComponents.get("COMPONENTS");

						compPortMap.put(componentName, portMap);
					} else {
						compPortMap = new HashMap<>();
						Map<String, Map<String, List<String>>> temp = new HashMap<>();

						temp.put(componentName, portMap);
						appComponents.put("COMPONENTS", temp);
					}
				} catch (ResourceNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			/*
			 * Check if tree is empty if so create initial
			 * 
			 * Underlying output should be APPLICATIONS +-<APP NAME> +--COMPONENTS +---<COMP
			 * NAME> +----PORTS +-----<PORT NAME>
			 */
			if (portTree.isEmpty()) {
				/*
				 * App name is child
				 */
				Map<String, Map<String, Map<String, Map<String, List<String>>>>> temp = new HashMap<>();
				temp.put(appName, appComponents);
				portTree.put("APPLICATIONS", temp);
			} else {
				// Get applications
				Map<String, Map<String, Map<String, Map<String, List<String>>>>> temp = portTree.get("APPLICATIONS");

				temp.put(appName, appComponents);
			}
		}

		return portTree;
	}
}

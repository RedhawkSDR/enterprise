package redhawk.driver.application;

import java.util.Map;

import CF.ApplicationFactory;


public interface RedhawkApplicationFactory {
	String getName();
	String getIdentifier();
	String getSoftwareProfile();
	Map<String, RedhawkApplication> getApplicationInstances();
//	RedhawkApplication createApplication(String instanceName, Map<String, Object> initialConfiguration, List<RedhawkDeviceAssignment> deviceAssignments ) throws ApplicationCreationException;
	ApplicationFactory getCorbaObject();
	void release();
}

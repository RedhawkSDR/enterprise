package redhawk.driver.devicemanager;

import org.omg.CosNaming.NamingContextPackage.NotFound;

public interface DeviceManagerInturruptedCallback {

	void deviceManagerReconnected();
	void deviceManagerDisconnected();
	void connectionProblem(String message);
	void lostDomainManagerConnection();
	void restoredDomainManagerConnection();
	void serviceReregistrationFailed(String message, Object serviceInformation, Class serviceInformation2, Class serviceInformation3, NotFound e);
	
}
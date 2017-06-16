package redhawk.driver.logging;

import redhawk.driver.RedhawkLogLevel;

public interface RedhawkLogging {
	RedhawkLogLevel getLogLevel();
	
	void setLogLevel(RedhawkLogLevel level);
}

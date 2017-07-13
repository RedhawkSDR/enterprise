package redhawk.driver.logging;

public interface RedhawkLogging {
	RedhawkLogLevel getLogLevel();
	
	void setLogLevel(RedhawkLogLevel level);
}

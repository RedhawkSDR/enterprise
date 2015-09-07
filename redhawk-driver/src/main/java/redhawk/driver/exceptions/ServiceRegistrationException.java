package redhawk.driver.exceptions;

public class ServiceRegistrationException extends Exception {

	public ServiceRegistrationException() {
		super();
	}

	public ServiceRegistrationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServiceRegistrationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceRegistrationException(String message) {
		super(message);
	}

	public ServiceRegistrationException(Throwable cause) {
		super(cause);
	}

}

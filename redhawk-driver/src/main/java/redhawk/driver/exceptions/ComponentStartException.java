package redhawk.driver.exceptions;

public class ComponentStartException extends Exception {

	public ComponentStartException() {
		super();
	}

	public ComponentStartException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ComponentStartException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComponentStartException(String message) {
		super(message);
	}

	public ComponentStartException(Throwable cause) {
		super(cause);
	}

}

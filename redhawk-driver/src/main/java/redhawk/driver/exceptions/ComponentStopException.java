package redhawk.driver.exceptions;

public class ComponentStopException extends Exception {

	public ComponentStopException() {
		super();
	}

	public ComponentStopException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ComponentStopException(String message, Throwable cause) {
		super(message, cause);
	}

	public ComponentStopException(String message) {
		super(message);
	}

	public ComponentStopException(Throwable cause) {
		super(cause);
	}

}

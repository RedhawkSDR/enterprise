package redhawk.driver.exceptions;

public class ApplicationStopException extends Exception {

	public ApplicationStopException() {
		super();
	}

	public ApplicationStopException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ApplicationStopException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationStopException(String message) {
		super(message);
	}

	public ApplicationStopException(Throwable cause) {
		super(cause);
	}

}

package redhawk.driver.exceptions;

public class ApplicationCreationException extends Exception {

	public ApplicationCreationException() {
		super();
	}

	public ApplicationCreationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ApplicationCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationCreationException(String message) {
		super(message);
	}

	public ApplicationCreationException(Throwable cause) {
		super(cause);
	}

}

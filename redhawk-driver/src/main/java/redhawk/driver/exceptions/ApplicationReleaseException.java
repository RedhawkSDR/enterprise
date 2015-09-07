package redhawk.driver.exceptions;

public class ApplicationReleaseException extends Exception {

	public ApplicationReleaseException() {
		super();
	}

	public ApplicationReleaseException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ApplicationReleaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationReleaseException(String message) {
		super(message);
	}

	public ApplicationReleaseException(Throwable cause) {
		super(cause);
	}

}

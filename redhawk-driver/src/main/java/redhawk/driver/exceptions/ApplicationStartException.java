package redhawk.driver.exceptions;

public class ApplicationStartException extends Exception {

	public ApplicationStartException() {
		super();
	}

	public ApplicationStartException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ApplicationStartException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationStartException(String message) {
		super(message);
	}

	public ApplicationStartException(Throwable cause) {
		super(cause);
	}

}

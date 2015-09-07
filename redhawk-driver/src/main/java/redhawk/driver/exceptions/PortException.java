package redhawk.driver.exceptions;

public class PortException extends Exception {

	private static final long serialVersionUID = 1L;

	public PortException() {
		super();
	}

	public PortException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PortException(String message, Throwable cause) {
		super(message, cause);
	}

	public PortException(String message) {
		super(message);
	}

	public PortException(Throwable cause) {
		super(cause);
	}

}

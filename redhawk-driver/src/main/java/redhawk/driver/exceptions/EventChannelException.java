package redhawk.driver.exceptions;

public class EventChannelException extends Exception {

	public EventChannelException() {
		super();
	}

	public EventChannelException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EventChannelException(String message, Throwable cause) {
		super(message, cause);
	}

	public EventChannelException(String message) {
		super(message);
	}

	public EventChannelException(Throwable cause) {
		super(cause);
	}

}

package redhawk.driver.exceptions;

public class EventChannelCreationException extends Exception {

	public EventChannelCreationException() {
		super();
	}

	public EventChannelCreationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EventChannelCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	public EventChannelCreationException(String message) {
		super(message);
	}

	public EventChannelCreationException(Throwable cause) {
		super(cause);
	}

}

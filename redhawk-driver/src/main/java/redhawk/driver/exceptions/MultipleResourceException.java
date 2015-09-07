package redhawk.driver.exceptions;

public class MultipleResourceException extends ResourceException {

    public MultipleResourceException() {
        super();
    }

    public MultipleResourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MultipleResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleResourceException(String message) {
        super(message);
    }

    public MultipleResourceException(Throwable cause) {
        super(cause);
    }

}

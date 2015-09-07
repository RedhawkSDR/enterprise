package redhawk.driver.exceptions;

public class CORBAException extends Exception {

    public CORBAException() {
        super();
    }

    public CORBAException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CORBAException(String message, Throwable cause) {
        super(message, cause);
    }

    public CORBAException(String message) {
        super(message);
    }

    public CORBAException(Throwable cause) {
        super(cause);
    }

}

package redhawk.rest.exceptions;
/**
 * RedhawkManagerRestException.java
 *
 * Base exception for all REDHAWK Manager Rest exceptions.
 */
public class RedhawkManagerRestException extends Throwable {

  public RedhawkManagerRestException() { super(); }
  public RedhawkManagerRestException(String message) { super(message); }

  public RedhawkManagerRestException(Exception e) { this(e.getClass().getSimpleName() + ":: " + e.getMessage()); }
}

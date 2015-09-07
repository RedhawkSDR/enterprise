package redhawk.rest.exceptions;
/**
 * RedhawkManagerException.java
 *
 * Base exception for all REDHAWK Manager exceptions.
 */
public class RedhawkManagerException extends Throwable {

  public RedhawkManagerException() { super(); }
  public RedhawkManagerException(String message) { super(message); }

  public RedhawkManagerException(Exception e) { this(e.getClass().getSimpleName() + ":: " + e.getMessage()); }
}

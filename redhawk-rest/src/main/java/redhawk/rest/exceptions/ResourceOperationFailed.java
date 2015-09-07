package redhawk.rest.exceptions;
/**
 * RedhawkManagerException.java
 *
 * Base exception for all REDHAWK Manager exceptions.
 */
public class ResourceOperationFailed extends RedhawkManagerException {

  public ResourceOperationFailed(String operation, String name) {
    this(operation, name, null);
  }

  public ResourceOperationFailed(String operation, String name, String reason) {
    super("Operation " + operation + " on '" + name + "' failed." + (reason == null ? "" : " Message: " + reason));
  }

}

package redhawk.rest.exceptions;
/**
 * ResourceNotFound.java
 *
 * REDHAWK Manager exception that indicates an error when configuring a property.
 */
public class PropertyConfigureFailed extends RedhawkManagerException {
  public PropertyConfigureFailed(String message) { super(message); }
  public PropertyConfigureFailed(Exception e) { super(e); }
}

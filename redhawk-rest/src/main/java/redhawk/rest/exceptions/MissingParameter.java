package redhawk.rest.exceptions;
/**
 * MissingParameter.java
 *
 * REDHAWK Manager exception that indicates a missing parameter from the request.
 */
public class MissingParameter extends RedhawkManagerRestException {
  public MissingParameter(String name) {
    super("Missing parameter '"+name+"' from request.");
  }
}

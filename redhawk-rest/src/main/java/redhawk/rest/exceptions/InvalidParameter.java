package redhawk.rest.exceptions;
/**
 * InvalidParameter.java
 *
 * REDHAWK Manager exception that indicates an invalid parameter from the request.
 */
public class InvalidParameter extends RedhawkManagerRestException {
  public InvalidParameter(String name, String type) {
    super("Invalid parameter '"+name+"' from request, expecting "+type+".");
  }
}


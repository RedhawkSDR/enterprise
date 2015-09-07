package redhawk.rest.exceptions;
/**
 * ResourceNotFound.java
 *
 * REDHAWK Manager exception that indicates a invalid resource.
 */
public class ResourceNotFound extends RedhawkManagerException {
  public ResourceNotFound(String type, String name) {
    super( "Unable to find "+type+" with name/id '"+name+"'");
  }
}

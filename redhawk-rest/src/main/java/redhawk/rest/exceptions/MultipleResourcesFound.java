package redhawk.rest.exceptions;
/**
 * MultipleResourcesFound.java
 *
 * REDHAWK Manager exception that indicates more than one resource for a given search.
 */
public class MultipleResourcesFound extends RedhawkManagerException {
  public MultipleResourcesFound(String type, String name) {
    super("More than one "+type+" found with name/id '"+name+"'");
  }
}
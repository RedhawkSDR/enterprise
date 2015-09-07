package redhawk.rest.model;
/**
 * IDL.java
 *
 * Bean class for the RedhawkManager
 */

public class IDL {
  public String namespace;
  public String type;
  public String version;

  public IDL(String repId) {
    this.type = repId.replaceFirst(".*/", "").replaceFirst(":.*", "");
    this.version = repId.replaceFirst(".*/", "").replaceFirst(".*:", "");
    this.namespace = repId.replaceFirst("/.*", "").replaceFirst("IDL:", "");
  }
}

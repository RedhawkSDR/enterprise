package redhawk.rest.model;
/**
 * Domain.java
 *
 * Bean class for the RedhawkManager
 */

public class CorbaPort {
  public String name;
  // TODO: Enum?
  public String direction;
  public String repId;
  public IDL idl;

  public CorbaPort(String name, String direction, String repId) {
    this(name, direction, repId, new IDL(repId));
  }

  public CorbaPort(String name, String direction, String repId, IDL idl) {
    this.repId = repId;
    this.name = name;
    this.direction = direction;
    this.idl = idl;
  }
}

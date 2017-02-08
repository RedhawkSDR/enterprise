/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
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

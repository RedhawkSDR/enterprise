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
package redhawk.rest.exceptions;
/**
 * RedhawkManagerRestException.java
 *
 * Base exception for all REDHAWK Manager Rest exceptions.
 */
public class RedhawkManagerRestException extends Throwable {

  public RedhawkManagerRestException() { super(); }
  public RedhawkManagerRestException(String message) { super(message); }

  public RedhawkManagerRestException(Exception e) { this(e.getClass().getSimpleName() + ":: " + e.getMessage()); }
}

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
package redhawk.driver.base.impl;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.omg.CORBA.ORB;

import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public abstract class CorbaBackedObject<TParsedClass> {
	/**
	 * Interoperable Object Reference 
	 */
	private String ior;
	
	private ORB orb;
	private Logger log = Logger.getLogger(CorbaBackedObject.class.getName());
	
	public CorbaBackedObject(String ior, ORB orb) {
		this.ior = ior;
		this.orb = orb;
	}

	/**
	 * Uses the given ORB and IOR to retrieve a POJO representing 
	 * the CORBA object. 
	 *  
	 * @return
	 * @throws ConnectionException
	 */
	public TParsedClass getCorbaObject() throws ConnectionException {
		TParsedClass object = (TParsedClass) orb.string_to_object(ior);
		try {
			((org.omg.CORBA.Object) object)._non_existent();
		} catch (Throwable e) {
			try {
				this.ior = orb.object_to_string((org.omg.CORBA.Object) locateCorbaObject());
			} catch (ResourceNotFoundException e1) {
				throw new ConnectionException(e1);
			}
		}

		Object result = null;
		Class<?> helperClass = getHelperClass();
		try {
			org.omg.CORBA.Object obj = orb.string_to_object(ior);
			Method narrowMethod = helperClass.getMethod("narrow", org.omg.CORBA.Object.class);
			result = narrowMethod.invoke(null, obj);
		} catch (Exception e) {
			log.warning("Error narrowing with helper : " + helperClass.getName() + " : " + e.getMessage());
		}
		return (TParsedClass) result;
	}

	protected abstract TParsedClass locateCorbaObject() throws ResourceNotFoundException;
	
	/**
	 * @return Interoperable Object Reference for this {@link CorbaBackedObject}
	 */
	public String getIor() {
		return ior;
	}
	
	/**
	 * Set Interoperable Object Reference for this {@link CorbaBackedObject}
	 * @param ior
	 */
	public void setIor(String ior){
		this.ior = ior;
	}

	/**
	 * @return Retrieve an {@link ORB} passed into this object. 
	 */
	public ORB getOrb() {
		return orb;
	}

	/**
	 * @return
	 *  Gets the Helper class for this CORBA object. 
	 */
	public abstract Class<?> getHelperClass();
}

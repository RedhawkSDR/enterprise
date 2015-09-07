package redhawk.driver.base.impl;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.omg.CORBA.ORB;

import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public abstract class CorbaBackedObject<TParsedClass> {

	private String ior;
	private ORB orb;
	private Logger log = Logger.getLogger(CorbaBackedObject.class.getName());

	public CorbaBackedObject(String ior, ORB orb) {
		this.ior = ior;
		this.orb = orb;
	}

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

	public String getIor() {
		return ior;
	}
	
	public void setIor(String ior){
		this.ior = ior;
	}

	public ORB getOrb() {
		return orb;
	}

	public abstract Class<?> getHelperClass();

}

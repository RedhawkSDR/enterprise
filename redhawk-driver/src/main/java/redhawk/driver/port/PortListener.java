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
package redhawk.driver.port;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import redhawk.driver.bulkio.Packet;


public abstract class PortListener<TParsedClass> {

	private static Logger logger = Logger.getLogger(PortListener.class.getName());
	private int maxQueueSize = 8000;
	
	public Class getPortType(){
		Type t = getClass().getGenericSuperclass();
		logger.info(t.getClass().getName());
		ParameterizedType p = (ParameterizedType) t;
		Class<?> serviceImplClass = (Class<?>) p.getActualTypeArguments()[0];
		return serviceImplClass;
	}
	
	public abstract void onReceive(Packet<TParsedClass> packet);

	public int getMaxQueueSize() {
		return maxQueueSize;
	}
	
	public void setMaxQueueSize(int maxQueueSize){
		this.maxQueueSize = maxQueueSize;
	}
	
}
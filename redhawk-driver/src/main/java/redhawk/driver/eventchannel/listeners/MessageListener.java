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
package redhawk.driver.eventchannel.listeners;

import java.util.Map;

import org.omg.CORBA.Any;
import org.omg.CORBA.TypeCodePackage.BadKind;

import redhawk.driver.RedhawkUtils;


public abstract class MessageListener extends EventChannelListener<Map<String,Object>> {

	@Override
	protected Map<String, Object> processMessage(Any data) {
		return RedhawkUtils.convertPropertiesAnyToMap(data);
	}
	
}
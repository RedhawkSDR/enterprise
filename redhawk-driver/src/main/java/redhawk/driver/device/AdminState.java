/*
g * This file is protected by Copyright. Please refer to the COPYRIGHT file
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
package redhawk.driver.device;

import java.util.HashMap;
import java.util.Map;

public enum AdminState {
	LOCKED(0),
	
	SHUTTING_DOWN(1),
	
	UNLOCKED(1);
	
	private Integer value; 
	
	private static final Map<Integer, AdminState> ADMINSTATES = new HashMap<>();
	
	static {
		for(AdminState value : values()) {
			ADMINSTATES.put(value.getValue(), value);
		}
	}
	
	AdminState(Integer value) {
		this.value = value;
	}
	
	public Integer getValue(){
		return value;
	}
	
	public static AdminState reverseLookup(Integer value) {
		AdminState admin = ADMINSTATES.get(value);
		
		if(admin==null) {
			throw new IllegalArgumentException("Unknown Admin State "+value);
		}
		
		return admin;
	}
}

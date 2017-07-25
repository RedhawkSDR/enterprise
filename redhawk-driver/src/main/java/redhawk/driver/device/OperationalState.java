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
package redhawk.driver.device;

import java.util.HashMap;
import java.util.Map;

public enum OperationalState {
	ENABLED(0),
	
	DISABLED(1);
	
	private Integer value; 
	
	private static final Map<Integer, OperationalState> OPERATIONALSTATES = new HashMap<>(); 
	
	static {
		for(OperationalState value : values()){
			OPERATIONALSTATES.put(value.getValue(), value);
		}
	}
	
	OperationalState(Integer value){
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public static OperationalState reverseLookup(Integer value) {
		OperationalState state = OPERATIONALSTATES.get(value);
		
		if(state==null) {
			throw new IllegalArgumentException("Unknown operation state "+value);
		}
		
		return state;
	}
}

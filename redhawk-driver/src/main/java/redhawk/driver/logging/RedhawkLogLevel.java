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
package redhawk.driver.logging;

import java.util.HashMap;
import java.util.Map;

public enum RedhawkLogLevel {
	OFF(60000),
	
	FATAL(50000),
	
	ERROR(40000),
	
	WARN(30000),
	
	INFO(20000),
	
	DEBUG(10000),
	
	TRACE(5000),
	
	ALL(0);
	
	private Integer value;
	
	private static final Map<Integer, RedhawkLogLevel> REHDAWKLOGLEVELS = new HashMap<>();
	
	static {
		for(RedhawkLogLevel value : values()){
			REHDAWKLOGLEVELS.put(value.getValue(), value);
		}
	}
	
	RedhawkLogLevel(Integer t){
		this.value = t;
	}
	
	public Integer getValue(){
		return value;
	}
	
	public static RedhawkLogLevel reverseLookup(Integer value){
		RedhawkLogLevel level = REHDAWKLOGLEVELS.get(value);
		if(level==null){
			throw new IllegalArgumentException("Unknown log level "+value);
		}
		
		return level;
	}
}

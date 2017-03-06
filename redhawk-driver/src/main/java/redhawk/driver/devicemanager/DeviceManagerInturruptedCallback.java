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
package redhawk.driver.devicemanager;

import org.omg.CosNaming.NamingContextPackage.NotFound;

public interface DeviceManagerInturruptedCallback {
	/**
	 * Logic for what a device manager should do when it reconnects. 
	 */
	void deviceManagerReconnected();
	
	/**
	 * Logic for what a device manager should do if disconnected. 
	 */
	void deviceManagerDisconnected();
	
	/**
	 * @param message 
	 * 	Message to output on connection problems. 
	 */
	void connectionProblem(String message);
	
	/**
	 * Logic for what do if domain manager connection is lost. 
	 */
	void lostDomainManagerConnection();
	
	/**
	 * Logic for what to do when restored connection to Domain Manager. 
	 */
	void restoredDomainManagerConnection();
	
	/**
	 * Message to output if Service registration fails. 
	 * @param message
	 * 	Message to output
	 * @param serviceInformation
	 * 	Service information
	 * @param serviceInformation2
	 * @param serviceInformation3
	 * @param e
	 */
	void serviceReregistrationFailed(String message, Object serviceInformation, Class serviceInformation2, Class serviceInformation3, NotFound e);
	
}
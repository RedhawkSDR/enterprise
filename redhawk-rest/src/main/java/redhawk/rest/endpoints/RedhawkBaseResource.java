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
package redhawk.rest.endpoints;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.cxf.rs.security.cors.LocalPreflight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redhawk.rest.RedhawkManager;

public class RedhawkBaseResource {
	private Logger logger = LoggerFactory.getLogger(RedhawkBaseResource.class);
	
	public RedhawkManager redhawkManager;
	
	public RedhawkBaseResource() {
		redhawkManager = new RedhawkManager();
	}
	
	/*@OPTIONS
	@Path("{id:.*}")
	@LocalPreflight	
	public Response getOptions() {
		return addCors(Response.ok()).build();
	}
	
	protected ResponseBuilder addCors(ResponseBuilder responseBuilder) {
		responseBuilder.header("Access-Control-Allow-Origin", "*");
		responseBuilder.header("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
		responseBuilder.header("Access-Control-Allow-Credentials", "true");
		responseBuilder.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		
		return responseBuilder;
	}
	*/
	
	
	public RedhawkManager getRedhawkManager() {
		return redhawkManager;
	}

	public void setRedhawkManager(RedhawkManager redhawkManager) {
		this.redhawkManager = redhawkManager;
	}
}

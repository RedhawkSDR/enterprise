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

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import redhawk.rest.exceptions.ResourceNotFound;

@Path("/{nameserver}/domains/{domain}/applications/{applicationId}/components/{componentId}/softwarecomponent")
@Api(value="/{nameserver}/domains/{domain}/applications/{applicationId}/components/{componentId}/softwarecomponent")
public class RedhawkSoftwareComponentResource extends RedhawkBaseResource{
    private static Logger logger = Logger.getLogger(RedhawkSoftwareComponentResource.class.getName());

	@PathParam("nameserver")
	private String nameServer;

	@PathParam("domain")
	private String domainName;

	@PathParam("applicationId")
	private String applicationId;

	@PathParam("componentId")
	private String componentId;

	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(
    		value="GET SCD for REDHAWK Component"
    		)	
	public Response getSoftwareComponent() throws ResourceNotFound, Exception {
		return Response.ok(redhawkManager.get(nameServer,
						"softwarecomponent", domainName + "/" + applicationId
								+ "/" + componentId)).build();
	}

}

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

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.Port;
import redhawk.rest.model.PortContainer;
import redhawk.rest.model.PortStatisticsContainer;
import redhawk.rest.model.SRIContainer;

@Path("/{nameserver}/domains/{domain}/applications/{applicationId}/components/{componentId}/ports")
@Api(value="component ports")
public class RedhawkPortsResource extends RedhawkBaseResource {
    private static Logger logger = Logger.getLogger(RedhawkPortsResource.class.getName());

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Component Ports"
    		)
    public PortContainer getPorts(
    	    @ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId,
    		@ApiParam(value = "Name of Component") @PathParam("componentId") String componentId) throws Exception {
        return new PortContainer(redhawkManager.getAll(nameServer, "port", domain + "/" + applicationId + "/" + componentId, FetchMode.EAGER));
    }

    @GET
    @Path("/{portId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Component Port"
    		)
    public Port getPort(
    		@ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId,
    		@ApiParam(value = "Name of Component") @PathParam("componentId") String componentId,
    		@PathParam("portId") String portName) throws Exception {
        return redhawkManager.get(nameServer, "port", domain + "/" + applicationId + "/" + componentId + "/" + portName);
    }

    @GET
    @Path("/{portId}/statistics")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Component Port Statistics"
    		)
    public PortStatisticsContainer getPortStatistics(
    		@ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId,
    		@ApiParam(value = "Name of Component") @PathParam("componentId") String componentId,
    		@PathParam("portId") String portName) throws Exception {
        return redhawkManager.getRhPortStatistics(nameServer, "port", domain + "/" + applicationId + "/" + componentId + "/" + portName);
    }
    
    @GET
    @Path("/{portId}/sri")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Component Port SRI"
    		)
    public SRIContainer getActiveSRIs(
    		@ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId,
    		@ApiParam(value = "Name of Component") @PathParam("componentId") String componentId,
    		@PathParam("portId") String portName) throws Exception {
        return redhawkManager.getSRI(nameServer, "port", domain + "/" + applicationId + "/" + componentId + "/" + portName);
    }
    
    @DELETE
    @Path("/{portId}/disconnect/{connectionId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(value = "DELETE a Connection from a Port by connectionId")
    public Response disconnectPortConnection(
    		@ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId,
    		@ApiParam(value = "Name of Component") @PathParam("componentId") String componentId,
    		@PathParam("portId") String portName,
    		@PathParam("connectionId") String connectionId){
    	String portPath = domain + "/" + applicationId + "/" + componentId + "/" + portName;
    	
    	try {
			redhawkManager.disconnectConnectionById(nameServer, "port", portPath, connectionId);
			return Response.ok("Disconnected "+connectionId).build();
    	} catch (Exception e) {
			throw new WebApplicationException("Error disconnecting Port", Response.Status.BAD_REQUEST);
		}
    }
}

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
import redhawk.rest.exceptions.ResourceNotFound;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.PortContainer;
import redhawk.rest.model.PortStatisticsContainer;
import redhawk.rest.model.SRIContainer;

@Path("/{nameserver}/domains/{domain}/applications/{applicationId}/components/{componentId}/ports")
@Api(value="/{nameserver}/domains/{domain}/applications/{applicationId}/components/{componentId}/ports")
public class RedhawkPortsResource extends RedhawkBaseResource {

    private static Logger logger = Logger.getLogger(RedhawkPortsResource.class.getName());

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
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Component Ports"
    		)
    public Response getPorts() throws ResourceNotFound, Exception {
        return Response.ok(new PortContainer(redhawkManager.getAll(nameServer, "port", domainName + "/" + applicationId + "/" + componentId, FetchMode.EAGER))).build();
    }

    @GET
    @Path("/{portId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Component Port"
    		)
    public Response getPort(@PathParam("portId") String portName) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.get(nameServer, "port", domainName + "/" + applicationId + "/" + componentId + "/" + portName)).build();
    }

    @GET
    @Path("/{portId}/statistics")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Component Port Statistics"
    		)
    public PortStatisticsContainer getPortStatistics(@PathParam("portId") String portName) throws Exception {
        return redhawkManager.getRhPortStatistics(nameServer, "port", domainName + "/" + applicationId + "/" + componentId + "/" + portName);
    }
    
    @GET
    @Path("/{portId}/sri")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Component Port SRI"
    		)
    public SRIContainer getActiveSRIs(@PathParam("portId") String portName) throws Exception {
        return redhawkManager.getSRI(nameServer, "port", domainName + "/" + applicationId + "/" + componentId + "/" + portName);
    }
    
    @DELETE
    @Path("/{portId}/disconnect/{connectionId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(value = "DELETE a Connection from a Port by connectionId")
    public Response disconnectPortConnection(
    		@PathParam("portId") String portName,
    		@PathParam("connectionId") String connectionId){
    	String portPath = domainName + "/" + applicationId + "/" + componentId + "/" + portName;
    	
    	try {
			redhawkManager.disconnectConnectionById(nameServer, "port", portPath, connectionId);
			return Response.ok("Disconnected "+connectionId).build();
    	} catch (Exception e) {
			throw new WebApplicationException("Error disconnecting Port", Response.Status.BAD_REQUEST);
		}
    }
}

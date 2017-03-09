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

import redhawk.rest.RedhawkManager;
import redhawk.rest.exceptions.ResourceNotFound;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.PortContainer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.logging.Logger;

@Path("/{nameserver}/domains/{domain}/applications/{applicationId}/components/{componentId}/ports")
@Api(value="/{nameserver}/domains/{domain}/applications/{applicationId}/components/{componentId}/ports")
public class RedhawkPortsResource extends RedhawkBaseResource {

    private static Logger logger = Logger.getLogger(RedhawkPortsResource.class.getName());

    private RedhawkManager redhawkManager;

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
    		value="Returns all ports for a component"
    		)
    public Response getPorts() throws ResourceNotFound, Exception {
        return Response.ok(new PortContainer(redhawkManager.getAll(nameServer, "port", domainName + "/" + applicationId + "/" + componentId, FetchMode.EAGER))).build();
    }

    @GET
    @Path("/{portId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Returns a specific port for a component"
    		)
    public Response getPort(@PathParam("portId") String portName) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.get(nameServer, "port", domainName + "/" + applicationId + "/" + componentId + "/" + portName)).build();
    }

    @GET
    @Path("/{portId}/statistics")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Returns port statistics for a component"
    		)
    public Response getPortStatistics(@PathParam("portId") String portName) throws Exception {
        return Response.ok(redhawkManager.getRhPortStatistics(nameServer, "port", domainName + "/" + applicationId + "/" + componentId + "/" + portName)).build();
    }
}
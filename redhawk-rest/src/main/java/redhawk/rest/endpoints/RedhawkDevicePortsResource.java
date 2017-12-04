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
import io.swagger.annotations.ApiParam;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.PortContainer;

@Path("/{nameserver}/domains/{domain}/devicemanagers/{devmanager}/devices/{deviceId}/ports")
@Api(value="device ports")
public class RedhawkDevicePortsResource extends RedhawkBaseResource{
    private static Logger logger = Logger.getLogger(RedhawkDevicePortsResource.class.getName());
    
    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Device Ports"
    		)
    public Response getPorts(
    		@ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "Label or Id for Device Manager", required = true) @PathParam("devmanager") String devManager,
    		@ApiParam(value = "Name or Id for Device", required = true) @PathParam("deviceId") String deviceId) throws Exception {
        return Response.ok(new PortContainer(redhawkManager.getAll(nameServer, "deviceport", domain + "/" + devManager + "/" + deviceId, FetchMode.EAGER))).build();
    }

    @GET
    @Path("/{portId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Device Port"
    		)
    public Response getPort(
    		@ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "Label or Id for Device Manager", required = true) @PathParam("devmanager") String devManager,
    		@ApiParam(value = "Name or Id for Device", required = true) @PathParam("deviceId") String deviceId,
    		@PathParam("portId") String portName) throws Exception {
        return Response.ok(redhawkManager.get(nameServer, "deviceport", domain + "/" + devManager + "/" + deviceId + "/" + portName)).build();
    }

    @GET
    @Path("/{portId}/statistics")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Port Statistics for a Device"
    		)
    public Response getPortStatistics(
    		@ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "Label or Id for Device Manager", required = true) @PathParam("devmanager") String devManager,
    		@ApiParam(value = "Name or Id for Device", required = true) @PathParam("deviceId") String deviceId,
    		@ApiParam(value = "Port name/id") @PathParam("portId") String portName) throws Exception {
        return Response.ok(redhawkManager.getRhPortStatistics(nameServer, "deviceport", domain + "/" + devManager + "/" + deviceId + "/" + portName)).build();
    }
}

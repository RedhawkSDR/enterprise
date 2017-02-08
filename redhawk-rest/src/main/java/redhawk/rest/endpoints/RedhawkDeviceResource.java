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

import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.rest.exceptions.ResourceNotFound;
import redhawk.rest.model.DeviceContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path("/{nameserver}/domains/{domain}/devicemanagers/{devmanager}/devices")
public class RedhawkDeviceResource extends RedhawkBaseResource {

    private static Logger logger = Logger.getLogger(RedhawkDeviceResource.class.getName());

    @PathParam("nameserver")
    private String nameServer;

    @PathParam("domain")
    private String domainName;

    @PathParam("devmanager")
    private String devManagerName;

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDevices(@QueryParam("fetch") @DefaultValue("EAGER") FetchMode fetchMode) throws ResourceNotFound, Exception {
        return Response.ok(new DeviceContainer(redhawkManager.getAll(nameServer, "device", domainName + "/" + devManagerName, fetchMode))).build();
    }

    @GET
    @Path("/{deviceId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDevice(@PathParam("deviceId") String deviceId) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.get(nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId)).build();
    }

    @GET
    @Path("/{deviceId}/properties")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDeviceProperties(@PathParam("deviceId") String deviceId) throws ResourceNotFound, ResourceNotFoundException, Exception {
        return Response.ok(redhawkManager.getProperties(nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId)).build();
    }

    @GET
    @Path("/{deviceId}/properties/{propId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDeviceProperty(@PathParam("deviceId") String deviceId, @PathParam("propId") String propertyId) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.getProperty(propertyId, nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId)).build();
    }

    @POST
    @Path("/{deviceId}/properties")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response setDeviceProperties(@PathParam("deviceId") String deviceId, List<FullProperty> properties) throws Exception {
        redhawkManager.setProperties(properties, nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId);
        return Response.ok().build();
    }

    @PUT
    @Path("/{deviceId}/properties/{propId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response setDeviceProperty(@PathParam("deviceId") String deviceId, @PathParam("propId") String propertyId, FullProperty property) throws Exception {
        redhawkManager.setProperty(property, nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId);
        return Response.ok().build();
    }
}
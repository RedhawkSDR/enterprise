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
import redhawk.rest.model.DeviceManagerContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;
import java.util.logging.Logger;

@Path("/{nameserver}/domains/{domain}/devicemanagers")
@Api(value="/{nameserver}/domains/{domain}/devicemanagers")
public class RedhawkDeviceManagerResource extends RedhawkBaseResource {

    private static Logger logger = Logger.getLogger(RedhawkDeviceManagerResource.class.getName());

    
    @ApiParam(value = "url for your name server")
    @PathParam("nameserver")
    private String nameServer;

    @ApiParam(value = "Name of REDHAWK Domain")
    @PathParam("domain")
    private String domainName;

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Returns all Device Managers for a Domain"
    		)
    public Response getDeviceManagers(@QueryParam("fetch") @DefaultValue("EAGER") FetchMode fetchMode) throws ResourceNotFound, Exception {
        return Response.ok(new DeviceManagerContainer(redhawkManager.getAll(nameServer, "devicemanager", domainName, fetchMode))).build();
    }

    @GET
    @Path("/{devMgrId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Returns a Specific Device Manager for a Domain."
    		)    
    public Response getDeviceManager(@PathParam("devMgrId") String deviceManagerId) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.get(nameServer, "devicemanager", domainName + "/" + deviceManagerId)).build();
    }
    
    @DELETE
    @Path("/{devMgrId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Deletes a Specific Device Manager from a Domain"
    		)    
    public Response shutDownDeviceManager(@PathParam("devMgrId") String deviceManagerId) throws ResourceNotFound, Exception {
        redhawkManager.shutdownDeviceManager(nameServer, domainName+"/"+deviceManagerId);
        
        return Response.ok().build();
    }

    @GET
    @Path("/{devMgrId}/properties")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Returns Properties for a Device Manager"
    		)    
    public Response getDeviceManagerProperties(@PathParam("devMgrId") String deviceManagerId) throws ResourceNotFound, ResourceNotFoundException, Exception {
        return Response.ok(redhawkManager.getProperties(nameServer, "devicemanager", domainName + "/" + deviceManagerId)).build();
    }

    @GET
    @Path("/{devMgrId}/properties/{propId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Returns a Specific Property for a Device Manager"
    		) 
    public Response getDeviceManagerProperty(@PathParam("devMgrId") String deviceManagerId, @PathParam("propId") String propertyId) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.getProperty(propertyId, nameServer, "devicemanager", domainName + "/" + deviceManagerId)).build();
    }

    @POST
    @Path("/{devMgrId}/properties")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Set a Property for a Device Manager"
    		) 
    public Response setDeviceManagerProperties(@PathParam("devMgrId") String deviceManagerId, List<FullProperty> properties) throws Exception {
        redhawkManager.setProperties(properties, nameServer, "devicemanager", domainName + "/" + deviceManagerId);
        return Response.ok().build();
    }

    @PUT
    @Path("/{devMgrId}/properties/{propId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Set a Property for a Device Manager"
    		)     
    public Response setDeviceManagerProperty(@PathParam("devMgrId") String deviceManagerId, @PathParam("propId") String propertyId, FullProperty property) throws Exception {
        redhawkManager.setProperty(property, nameServer, "devicemanager", domainName + "/" + deviceManagerId);
        return Response.ok().build();
    }
}


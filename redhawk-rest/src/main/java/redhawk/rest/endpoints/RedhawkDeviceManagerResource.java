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

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.rest.exceptions.ResourceNotFound;
import redhawk.rest.model.DeviceManager;
import redhawk.rest.model.DeviceManagerContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;

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
    		value="GET REDHAWK Device Managers"
    		)
    public DeviceManagerContainer getDeviceManagers(@QueryParam("fetch") @DefaultValue("EAGER") FetchMode fetchMode) throws ResourceNotFound, Exception {
        return new DeviceManagerContainer(redhawkManager.getAll(nameServer, "devicemanager", domainName, fetchMode));
    }

    @GET
    @Path("/{devMgrId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Device Manager"
    		)    
    public DeviceManager getDeviceManager(@PathParam("devMgrId") String deviceManagerId) throws ResourceNotFound, Exception {
        return redhawkManager.get(nameServer, "devicemanager", domainName + "/" + deviceManagerId);
    }
    
    @DELETE
    @Path("/{devMgrId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="DELETE REDHAWK Device Manager"
    		)    
    public Response shutDownDeviceManager(@PathParam("devMgrId") String deviceManagerId) throws ResourceNotFound, Exception {
        redhawkManager.shutdownDeviceManager(nameServer, domainName+"/"+deviceManagerId);
        
        return Response.ok().build();
    }

    @GET
    @Path("/{devMgrId}/properties")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Device Manager Properties"
    		)    
    public Response getDeviceManagerProperties(@PathParam("devMgrId") String deviceManagerId) throws ResourceNotFound, ResourceNotFoundException, Exception {
        return Response.ok(redhawkManager.getProperties(nameServer, "devicemanager", domainName + "/" + deviceManagerId)).build();
    }

    @GET
    @Path("/{devMgrId}/properties/{propId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Device Manager Property"
    		) 
    public Response getDeviceManagerProperty(@PathParam("devMgrId") String deviceManagerId, @PathParam("propId") String propertyId) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.getProperty(propertyId, nameServer, "devicemanager", domainName + "/" + deviceManagerId)).build();
    }

    @POST
    @Path("/{devMgrId}/properties")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Set REDHAWK Device Manager properties"
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
    		value="Set REDHAWK Device Manager Property"
    		)     
    public Response setDeviceManagerProperty(@PathParam("devMgrId") String deviceManagerId, @PathParam("propId") String propertyId, FullProperty property) throws Exception {
        redhawkManager.setProperty(property, nameServer, "devicemanager", domainName + "/" + deviceManagerId);
        return Response.ok().build();
    }
}


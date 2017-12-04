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
import redhawk.rest.model.DeviceManager;
import redhawk.rest.model.DeviceManagerContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;
import redhawk.rest.model.Property;
import redhawk.rest.model.PropertyContainer;

@Path("/{nameserver}/domains/{domain}/devicemanagers")
@Api(value="devicemanagers")
public class RedhawkDeviceManagerResource extends RedhawkBaseResource {
    private static Logger logger = Logger.getLogger(RedhawkDeviceManagerResource.class.getName());

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Device Managers"
    		)
    public DeviceManagerContainer getDeviceManagers(
    	    @ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@QueryParam("fetch") @DefaultValue("EAGER") FetchMode fetchMode) throws Exception {
        return new DeviceManagerContainer(redhawkManager.getAll(nameServer, "devicemanager", domain, fetchMode));
    }

    @GET
    @Path("/{devmanager}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Device Manager"
    		)    
    public DeviceManager getDeviceManager(
    	    @ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "Label or Id for Device Manager", required = true) @PathParam("devmanager") String deviceManagerId) throws Exception {
        return redhawkManager.get(nameServer, "devicemanager", domain + "/" + deviceManagerId);
    }
    
    @DELETE
    @Path("/{devmanager}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="DELETE REDHAWK Device Manager"
    		)    
    public Response shutDownDeviceManager(
    	    @ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "Label or Id for Device Manager", required = true) @PathParam("devmanager") String deviceManagerId) throws Exception {
        redhawkManager.shutdownDeviceManager(nameServer, domain+"/"+deviceManagerId);
        
        return Response.ok().build();
    }

    @GET
    @Path("/{devmanager}/properties")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Device Manager Properties"
    		)    
    public PropertyContainer getDeviceManagerProperties(
    	    @ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "Label or Id for Device Manager", required = true) @PathParam("devmanager") String deviceManagerId) throws ResourceNotFoundException, Exception {
        return redhawkManager.getProperties(nameServer, "devicemanager", domain + "/" + deviceManagerId);
    }

    @GET
    @Path("/{devmanager}/properties/{propId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Device Manager Property"
    		) 
    public Property getDeviceManagerProperty(
    	    @ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "Label or Id for Device Manager", required = true) @PathParam("devmanager") String deviceManagerId, @PathParam("propId") String propertyId) throws Exception {
        return redhawkManager.getProperty(propertyId, nameServer, "devicemanager", domain + "/" + deviceManagerId);
    }

    @POST
    @Path("/{devmanager}/properties")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Set REDHAWK Device Manager properties"
    		) 
    public Response setDeviceManagerProperties(
    	    @ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "Label or Id for Device Manager", required = true) @PathParam("devmanager") String deviceManagerId, @ApiParam(value="List of updates to make to DeviceManager properties", required=true) List<FullProperty> properties) throws Exception {
        redhawkManager.setProperties(properties, nameServer, "devicemanager", domain + "/" + deviceManagerId);
        return Response.ok().build();
    }

    @PUT
    @Path("/{devmanager}/properties/{propId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Set REDHAWK Device Manager Property"
    		)     
    public Response setDeviceManagerProperty(
    	    @ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "Label or Id for Device Manager", required = true) @PathParam("devmanager") String deviceManagerId, @PathParam("propId") String propertyId,@ApiParam(value="Updates to make to property", required=true) FullProperty property) throws Exception {
        redhawkManager.setProperty(property, nameServer, "devicemanager", domain + "/" + deviceManagerId);
        return Response.ok().build();
    }
}


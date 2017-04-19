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
import redhawk.rest.model.ComponentContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.logging.Logger;

@Path("/{nameserver}/domains/{domain}/applications/{applicationId}/components")
@Api(value = "/{nameserver}/domains/{domain}/applications/{applicationId}/components")
public class RedhawkComponentResource extends RedhawkBaseResource {

    private static Logger logger = Logger.getLogger(RedhawkComponentResource.class.getName());
    
    @ApiParam(value = "url for your name server")
    @PathParam("nameserver")
    private String nameServer;

    @ApiParam(value = "name of REDHAWK Domain")
    @PathParam("domain")
    private String domainName;

    @ApiParam(value = "name of Application")
    @PathParam("applicationId")
    private String applicationId;

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Returns all Components for an Application"
    		)    
    public Response getComponents(@QueryParam("fetch") @DefaultValue("EAGER") FetchMode fetchMode) throws ResourceNotFound, Exception {
        return Response.ok(new ComponentContainer(redhawkManager.getAll(nameServer, "component", domainName + "/" + applicationId, fetchMode))).build();
    }

    @GET
    @Path("/{componentId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Returns a Specific Component for an Application"
    		)     
    public Response getComponent(@ApiParam(value = "name of component")
    	@PathParam("componentId") String componentId) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.get(nameServer, "component", domainName + "/" + applicationId + "/" + componentId)).build();
    }

    @GET
    @Path("/{componentId}/properties")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Returns Properties for a Specific Component"
    		)    
    public Response getComponentProperties(@PathParam("componentId") String componentId) throws ResourceNotFound, ResourceNotFoundException, Exception {
        return Response.ok(redhawkManager.getProperties(nameServer, "component", domainName + "/" + applicationId + "/" + componentId)).build();
    }

    @GET
    @Path("/{componentId}/properties/{propId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Returns a Specific Property for a Component"
    		)     
    public Response getComponentProperty(@ApiParam(value = "name of component") @PathParam("componentId") String componentId, 
    		@ApiParam(value = "name of property") @PathParam("propId") String propertyId) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.getProperty(propertyId, nameServer, "component", domainName + "/" + applicationId + "/" + componentId)).build();
    }

//	@GET
//	@Path("/{id}/properties/{propId}/structsequence")
//	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//	public Response getApplicationProperties1(@PathParam("id") String componentId, @PathParam("propId") String propertyId) throws ResourceNotFound, Exception {
//		Property prop = redhawkManager.getProperty(propertyId, nameServer, "component", domainName+"/"+applicationId+"/"+componentId);
//		if(prop.getStructSequence() != null){
//			return Response.ok(prop.getStructSequence()).build();
//		} else {
//			return Response.status(Status.NOT_FOUND).build();
//		}
//	}


    @PUT
    @Path("/{componentId}/properties/{propId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Set a Property on a Specific Component"
    		)   
    public Response setComponentProperty(@PathParam("componentId") String componentId, @PathParam("propId") String propertyId, FullProperty property) throws Exception {
        redhawkManager.setProperty(property, nameServer, "component", domainName + "/" + applicationId + "/" + componentId);
        return Response.ok().build();
    }

}

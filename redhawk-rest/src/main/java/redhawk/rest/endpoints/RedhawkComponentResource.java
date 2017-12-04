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

import javax.ws.rs.Consumes;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.xml.model.sca.scd.Softwarecomponent;
import redhawk.rest.model.Component;
import redhawk.rest.model.ComponentContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;
import redhawk.rest.model.Property;
import redhawk.rest.model.PropertyContainer;

@Path("/{nameserver}/domains/{domain}/applications/{applicationId}/components")
@Api(value = "components")
public class RedhawkComponentResource extends RedhawkBaseResource {
	private static Logger logger = LoggerFactory.getLogger(RedhawkComponentResource.class.getName());

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET Application Components"
    		)    
    public ComponentContainer getComponents(
    	    @ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId,
    		@QueryParam("fetch") @DefaultValue("EAGER") FetchMode fetchMode) throws Exception {
        return new ComponentContainer(redhawkManager.getAll(nameServer, "component", domain + "/" + applicationId, fetchMode));
    }

    @GET
    @Path("/{componentId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET Application Component"
    		)     
    public Component getComponent(
    	    @ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId,
    		@ApiParam(value = "Name of Component") @PathParam("componentId") String componentId) throws Exception {
        return redhawkManager.get(nameServer, "component", domain + "/" + applicationId + "/" + componentId);
    }
    
	@POST
	@Path("/{componentId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Stop/Start a Component")
	public Response controlComponent(
    	    @ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId,
			@ApiParam(value = "Name of Component") @PathParam("componentId") String componentId, @ApiParam(value="Action to take on application start/stop", required=true) String control)
			throws Exception {
		redhawkManager.controlComponent(nameServer, control, domain+'/'+applicationId+'/'+componentId);
		
		return Response.ok().build();
	}
	

	@GET
	@Path("/{componentId}/softwarecomponent")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(
    		value="GET SCD for REDHAWK Component"
    		)	
	public Softwarecomponent getSoftwareComponent(
			@ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
	    	@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
	    	@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId,
			@ApiParam(value = "Name of Component") @PathParam("componentId") String componentId) throws Exception {
		return redhawkManager.get(nameServer,
						"softwarecomponent", domain + "/" + applicationId
								+ "/" + componentId);
	}

    @GET
    @Path("/{componentId}/properties")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET Application Component Properties"
    		)    
    public PropertyContainer getComponentProperties(
    	    @ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId,
    		@PathParam("componentId") String componentId) throws ResourceNotFoundException, Exception {
        return redhawkManager.getProperties(nameServer, "component", domain + "/" + applicationId + "/" + componentId);
    }

    @GET
    @Path("/{componentId}/properties/{propId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET Application Component Property"
    		)     
    public Property getComponentProperty(@ApiParam(value = "Name of Component") @PathParam("componentId") String componentId, 
    	    @ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId,
    		@ApiParam(value = "Name of Property") @PathParam("propId") String propertyId) throws Exception {
    	return redhawkManager.getProperty(propertyId, nameServer, "component", domain + "/" + applicationId + "/" + componentId);
    }

    @PUT
    @Path("/{componentId}/properties/{propId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Set Application Component Property"
    		)   
    public Response setComponentProperty(
    	    @ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
    		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId,
    		@PathParam("componentId") String componentId, @PathParam("propId") String propertyId,@ApiParam(value="Information to update property", required=true) FullProperty property) throws Exception {
        redhawkManager.setProperty(property, nameServer, "component", domain + "/" + applicationId + "/" + componentId);
        return Response.ok().build();
    }
}

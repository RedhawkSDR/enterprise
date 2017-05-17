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
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.rest.exceptions.ResourceNotFound;
import redhawk.rest.model.ApplicationContainer;
import redhawk.rest.model.ExternalPort;
import redhawk.rest.model.ExternalPortContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;
import redhawk.rest.model.Port;
import redhawk.rest.model.PortContainer;
import redhawk.rest.model.Property;
import redhawk.rest.model.PropertyContainer;
import redhawk.rest.model.WaveformInfo;

@Path("/{nameserver}/domains/{domain}/applications")
@Api(value="/{nameserver}/domains/{domain}/applications")
public class RedhawkApplicationResource extends RedhawkBaseResource {

    private static Logger logger = Logger.getLogger(RedhawkApplicationResource.class.getName());

    @ApiParam(value = "url for your name server", required = true)
    @PathParam("nameserver")
    private String nameServer;

    @ApiParam(value = "Name of REDHAWK Domain", required = true)
    @PathParam("domain")
    private String domainName;

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET Applications for a REDHAWK Domain"
    		)
    public ApplicationContainer getApplications(@QueryParam("fetch") @DefaultValue("EAGER") FetchMode fetchMode) throws ResourceNotFound, Exception {
    	return new ApplicationContainer(redhawkManager.getAll(nameServer,
                        "application", domainName, fetchMode));
    }

    @GET
    @Path("/{applicationId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET Application for a REDHAWK Domain"
    		)
    public Response getApplication(@PathParam("applicationId") String applicationId)
            throws ResourceNotFound, Exception {
        return Response.ok(
                redhawkManager.get(nameServer, "application", domainName + "/"
                        + applicationId)).build();
    }

    @DELETE
    @Path("/{applicationId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Release Application from a REDHAWK Domain"
    		)
    public Response releaseApplication(@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId)
            throws ResourceNotFound, Exception {
        redhawkManager
                .releaseApplication(nameServer, domainName, applicationId);
        return Response.ok().build();
    }
    
    @POST
    @Path("/{applicationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(
    		value="Stop/Start Application"
    		)
    public Response controlApplication(@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId, String control) throws Exception{
    	redhawkManager.controlApplication(nameServer, domainName, applicationId, control);
    	return Response.ok().build();
    }

    @PUT
    @Path("/{instanceName}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Launch Application"
    		)
    public Response launchApplication(@ApiParam(value = "Name for Application")
            @PathParam("instanceName") String instanceName, WaveformInfo info)
            throws ResourceNotFoundException, ApplicationCreationException {
        redhawkManager.createApplication(nameServer, domainName, instanceName,
                info);
        return Response.ok("Success").build();
    }

    @GET
    @Path("/{applicationId}/properties")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET Application Properties"
    		)    
    public PropertyContainer getApplicationProperties(@ApiParam(value = "ID for Application")
            @PathParam("applicationId") String applicationId) throws ResourceNotFound,
            ResourceNotFoundException, Exception {
        return redhawkManager.getProperties(nameServer, "application",
                        domainName + "/" + applicationId);
    }

    @GET
    @Path("/{applicationId}/properties/{propId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET Application Property"
    		)    
    public Property getApplicationProperty(
    		@ApiParam(value = "ID/Name for Application") @PathParam("applicationId") String applicationId,
    		@ApiParam(value = "ID/Name for Property") @PathParam("propId") String propertyId) throws ResourceNotFound,
            Exception {
        return redhawkManager.getProperty(propertyId, nameServer,
                        "application", domainName + "/" + applicationId);
    }

    @PUT
    @Path("/{applicationId}/properties/{propId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Set Application Property"
    		)    
    public Response setApplicationProperty(
    		@ApiParam(value = "ID for Application") @PathParam("applicationId") String applicationId, 
    		@ApiParam(value = "ID/Name for Property") @PathParam("propId") String propertyId, FullProperty property)
            throws Exception {
        redhawkManager.setProperty(property, nameServer, "application", domainName + "/" + applicationId);
        return Response.ok().build();
    }
    
    @GET
    @Path("/{applicationId}/ports")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET Application ports"
    )
	public ExternalPortContainer getApplicationPorts(@ApiParam(value = "ID/Name for Application") @PathParam("applicationId") String applicationId) throws ResourceNotFoundException, Exception{
		return new ExternalPortContainer(redhawkManager.getAll(nameServer, "applicationport", domainName+"/"+applicationId, FetchMode.EAGER));
	}
    
    @GET
    @Path("/{applicationId}/ports/{portId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET Application port"
    )
    public ExternalPort getApplicationPort(
    		@ApiParam(value = "ID/Name for Application") @PathParam("applicationId") String applicationId,
    		@PathParam("portId") String portName) throws ResourceNotFound, Exception {
        return redhawkManager.get(nameServer, "applicationport", domainName + "/" + applicationId + "/", portName);
    }
}

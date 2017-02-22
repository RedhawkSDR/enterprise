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

import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.rest.exceptions.ResourceNotFound;
import redhawk.rest.model.ApplicationContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;
import redhawk.rest.model.WaveformInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/{nameserver}/domains/{domain}/applications")
public class RedhawkApplicationResource extends RedhawkBaseResource {

    private static Logger logger = Logger.getLogger(RedhawkApplicationResource.class.getName());

    @PathParam("nameserver")
    private String nameServer;

    @PathParam("domain")
    private String domainName;

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getApplications(@QueryParam("fetch") @DefaultValue("EAGER") FetchMode fetchMode) throws ResourceNotFound, Exception {
        return Response.ok(
                new ApplicationContainer(redhawkManager.getAll(nameServer,
                        "application", domainName, fetchMode))).build();
    }

    @GET
    @Path("/{applicationId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getApplication(@PathParam("applicationId") String applicationId)
            throws ResourceNotFound, Exception {
        return Response.ok(
                redhawkManager.get(nameServer, "application", domainName + "/"
                        + applicationId)).build();
    }

    @DELETE
    @Path("/{applicationId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response releaseApplication(@PathParam("applicationId") String applicationId)
            throws ResourceNotFound, Exception {
        redhawkManager
                .releaseApplication(nameServer, domainName, applicationId);
        return Response.ok().build();
    }

    @PUT
    @Path("/{instanceName}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response launchApplication(
            @PathParam("instanceName") String instanceName, WaveformInfo info)
            throws ResourceNotFoundException, ApplicationCreationException {
        redhawkManager.createApplication(nameServer, domainName, instanceName,
                info);
        return Response.ok().build();
    }

    @GET
    @Path("/{applicationId}/properties")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getApplicationProperties(
            @PathParam("applicationId") String applicationId) throws ResourceNotFound,
            ResourceNotFoundException, Exception {
        return Response.ok(
                redhawkManager.getProperties(nameServer, "application",
                        domainName + "/" + applicationId)).build();
    }

    @GET
    @Path("/{applicationId}/properties/{propId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getApplicationProperty(
            @PathParam("applicationId") String applicationId,
            @PathParam("propId") String propertyId) throws ResourceNotFound,
            Exception {
        return Response.ok(
                redhawkManager.getProperty(propertyId, nameServer,
                        "application", domainName + "/" + applicationId))
                .build();
    }

    @PUT
    @Path("/{applicationId}/properties/{propId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response setApplicationProperty(
            @PathParam("applicationId") String applicationId, @PathParam("propId") String propertyId, FullProperty property)
            throws Exception {
        redhawkManager.setProperty(property, nameServer, "application", domainName + "/" + applicationId);
        return Response.ok().build();
    }
}
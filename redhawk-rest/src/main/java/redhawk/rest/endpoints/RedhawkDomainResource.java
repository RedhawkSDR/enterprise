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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.logging.Logger;

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

import redhawk.rest.exceptions.ResourceOperationFailed;
import redhawk.rest.model.Domain;
import redhawk.rest.model.DomainContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;
import redhawk.rest.model.PropertyContainer;

@Path("/{nameserver}/domains")
@Api(value = "/{nameserver}/domains", description = "REST Service for inspecting a REDHAWK Domain")
public class RedhawkDomainResource extends RedhawkBaseResource {

    private static Logger logger = Logger.getLogger(RedhawkDomainResource.class.getName());

    @PathParam("nameserver")
    private String nameServer;

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value = "Return the REDHAWK Domains based on the parameters",
    		response = DomainContainer.class
    		)
    public Response getDomains(@QueryParam("fetch") @DefaultValue("EAGER") FetchMode fetchMode) throws ResourceOperationFailed, Exception {
        List<Domain> domains = redhawkManager.getAll(nameServer, "domain", null, fetchMode);
        return Response.ok(new DomainContainer(domains)).build();
    }

    @GET
    @Path("/{domain}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
    @ApiOperation(
    		value = "Blah"
    		)
    public Response getDomain(@PathParam("domain") String name) {
        try {
            Domain domain = redhawkManager.get(nameServer, "domain", name);
            return Response.ok(domain).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
    }

    @GET
    @Path("/{domain}/properties")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value = "Blah"
    		)    
    public Response getDomainProperties(@PathParam("domain") String name) throws Exception {
        PropertyContainer props = redhawkManager.getProperties(nameServer, "domain", name);
        return Response.ok(props).build();
    }

    @GET
    @Path("/{domain}/properties/{propId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value = "Blah"
    		)    
    public Response getDomainPropertyById(@PathParam("domain") String name, @PathParam("propId") String id) throws Exception {
        return Response.ok(redhawkManager.getProperty(id, nameServer, "domain", name)).build();
    }

    @POST
    @Path("/{domain}/properties")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value = "Blah"
    		)
    public Response setDomainProperties(@PathParam("domain") String name, List<FullProperty> properties) throws Exception {
        redhawkManager.setProperties(properties, nameServer, "domain", name);
        return Response.ok().build();
    }


    @PUT
    @Path("/{domain}/properties/{propId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value = "Blah"
    		)    
    public Response setDomainProperty(@PathParam("domain") String name, @PathParam("propId") String propertyId, FullProperty property) throws Exception {
        redhawkManager.setProperty(property, nameServer, "domain", name);
        return Response.ok().build();
    }

}
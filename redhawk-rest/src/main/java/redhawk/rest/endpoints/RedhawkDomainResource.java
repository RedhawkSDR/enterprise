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
import io.swagger.annotations.ApiParam;

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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import redhawk.rest.exceptions.ResourceOperationFailed;
import redhawk.rest.model.Domain;
import redhawk.rest.model.DomainContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;
import redhawk.rest.model.Property;
import redhawk.rest.model.PropertyContainer;
import redhawk.rest.model.RegisterRemoteDomain;

@Path("/{nameserver}/domains")
@Api(value = "/{nameserver}/domains")
public class RedhawkDomainResource extends RedhawkBaseResource {

    private static Logger logger = Logger.getLogger(RedhawkDomainResource.class.getName());
    
    @ApiParam(value = "url for your name server", required = true)
    @PathParam("nameserver")
    private String nameServer;

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value = "GET REDHAWK Domains"
    		)
    public DomainContainer getDomains(@QueryParam("fetch") @DefaultValue("EAGER") FetchMode fetchMode) throws ResourceOperationFailed, Exception {
        List<Domain> domains = redhawkManager.getAll(nameServer, "domain", null, fetchMode);
        return new DomainContainer(domains);
    }

    @GET
    @Path("/{domain}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
    @ApiOperation(
    		value = "GET REDHAWK Domain"
    		)
    public Domain getDomain(@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String name) {
        try {
            Domain domain = redhawkManager.get(nameServer, "domain", name);
            return domain;
        } catch (Exception e) {
        	throw new WebApplicationException(e.getMessage(), Response.Status.NOT_FOUND);
        }
    }
    
    @GET
    @Path("/{domain}/properties")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value = "GET REDHAWK Domain Properties"
    		)    
    public PropertyContainer getDomainProperties(@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String name) throws Exception {
        PropertyContainer props = redhawkManager.getProperties(nameServer, "domain", name);

        return props;
    }

    @GET
    @Path("/{domain}/properties/{propId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value = "GET REDHAWK Domain Property"
    		)    
    public Property getDomainPropertyById(@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String name,@ApiParam(value = "Property name/ID", required = true) @PathParam("propId") String id) throws Exception {
        return redhawkManager.getProperty(id, nameServer, "domain", name);
    }

    @POST
    @Path("/{domain}/properties")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value = "Set Properties on a REDHAWK Domain"
    		)
    public Response setDomainProperties(@ApiParam(value = "Name of REDHAWK Domain") @PathParam("domain") String name, @ApiParam(value = "List of Properties to set", required=true) List<FullProperty> properties) throws Exception {
        redhawkManager.setProperties(properties, nameServer, "domain", name);
        return Response.ok().build();
    }


    @PUT
    @Path("/{domain}/properties/{propId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value = "Set a Property on a REDHAWK Domain"
    		)    
    public Response setDomainProperty(@ApiParam(value = "Name of REDHAWK Domain") @PathParam("domain") String name,
    		@ApiParam(value = "Property name/ID") @PathParam("propId") String propertyId, 
    		@ApiParam(value = "Property to set", required=true) FullProperty property) throws Exception {
        redhawkManager.setProperty(property, nameServer, "domain", name);
        return Response.ok().build();
    }
    
    @POST
    @Path("/{domain}/registerremotedomain")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value = "Register a Remote REDHAWK Domain"
    		) 
    public Response registerRemoteDomain(@ApiParam(value = "Name of REDHAWK Domain") @PathParam("domain") String name,
    		@ApiParam(value = "Remote Domain Information", required=true) RemoteDomainRegistrar registerRequest) throws Exception {
        redhawkManager.registerRemoteDomain(nameServer, "domain", name, registerRequest);
    	return Response.ok().build();
    }
    
    @DELETE
    @Path("/{domain}/unregisterremotedomain")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value = "Set a Property on a REDHAWK Domain"
    		) 
    public Response unregisterRemoteDomain(@ApiParam(value = "Name of REDHAWK Domain") @PathParam("domain") String name,
    		@ApiParam(value = "Remote Domain Name", required=true) String remoteDomainName) throws Exception {
        redhawkManager.unregisterRemoteDomain(nameServer, "domain", name, remoteDomainName);
    	return Response.ok().build();
    }

}

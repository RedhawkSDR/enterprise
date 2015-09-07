package redhawk.rest.endpoints;

import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.rest.exceptions.ResourceNotFound;
import redhawk.rest.model.ComponentContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/{nameserver}/domains/{domain}/applications/{applicationId}/components")
public class RedhawkComponentResource extends RedhawkBaseResource {

    private static Logger logger = Logger.getLogger(RedhawkComponentResource.class.getName());

    @PathParam("nameserver")
    private String nameServer;

    @PathParam("domain")
    private String domainName;

    @PathParam("applicationId")
    private String applicationId;

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getComponents(@QueryParam("fetch") @DefaultValue("EAGER") FetchMode fetchMode) throws ResourceNotFound, Exception {
        return Response.ok(new ComponentContainer(redhawkManager.getAll(nameServer, "component", domainName + "/" + applicationId, fetchMode))).build();
    }

    @GET
    @Path("/{componentId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getComponent(@PathParam("componentId") String componentId) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.get(nameServer, "component", domainName + "/" + applicationId + "/" + componentId)).build();
    }

    @GET
    @Path("/{componentId}/properties")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getComponentProperties(@PathParam("componentId") String componentId) throws ResourceNotFound, ResourceNotFoundException, Exception {
        return Response.ok(redhawkManager.getProperties(nameServer, "component", domainName + "/" + applicationId + "/" + componentId)).build();
    }

    @GET
    @Path("/{componentId}/properties/{propId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getComponentProperty(@PathParam("componentId") String componentId, @PathParam("propId") String propertyId) throws ResourceNotFound, Exception {
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
    public Response setComponentProperty(@PathParam("componentId") String componentId, @PathParam("propId") String propertyId, FullProperty property) throws Exception {
        redhawkManager.setProperty(property, nameServer, "component", domainName + "/" + applicationId + "/" + componentId);
        return Response.ok().build();
    }

}
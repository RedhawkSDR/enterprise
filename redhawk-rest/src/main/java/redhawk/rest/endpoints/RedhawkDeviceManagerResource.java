package redhawk.rest.endpoints;

import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.rest.exceptions.ResourceNotFound;
import redhawk.rest.model.DeviceManagerContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path("/{nameserver}/domains/{domain}/devicemanagers")
public class RedhawkDeviceManagerResource extends RedhawkBaseResource {

    private static Logger logger = Logger.getLogger(RedhawkDeviceManagerResource.class.getName());

    @PathParam("nameserver")
    private String nameServer;

    @PathParam("domain")
    private String domainName;

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDeviceManagers(@QueryParam("fetch") @DefaultValue("EAGER") FetchMode fetchMode) throws ResourceNotFound, Exception {
        return Response.ok(new DeviceManagerContainer(redhawkManager.getAll(nameServer, "devicemanager", domainName, fetchMode))).build();
    }

    @GET
    @Path("/{devMgrId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDeviceManagers(@PathParam("devMgrId") String deviceManagerId) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.get(nameServer, "devicemanager", domainName + "/" + deviceManagerId)).build();
    }

    @GET
    @Path("/{devMgrId}/properties")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDeviceManagerProperties(@PathParam("devMgrId") String deviceManagerId) throws ResourceNotFound, ResourceNotFoundException, Exception {
        return Response.ok(redhawkManager.getProperties(nameServer, "devicemanager", domainName + "/" + deviceManagerId)).build();
    }

    @GET
    @Path("/{devMgrId}/properties/{propId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDeviceManagerProperty(@PathParam("devMgrId") String deviceManagerId, @PathParam("propId") String propertyId) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.getProperty(propertyId, nameServer, "devicemanager", domainName + "/" + deviceManagerId)).build();
    }

    @POST
    @Path("/{devMgrId}/properties")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response setDeviceManagerProperties(@PathParam("devMgrId") String deviceManagerId, List<FullProperty> properties) throws Exception {
        redhawkManager.setProperties(properties, nameServer, "devicemanager", domainName + "/" + deviceManagerId);
        return Response.ok().build();
    }

    @PUT
    @Path("/{devMgrId}/properties/{propId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response setDeviceManagerProperty(@PathParam("devMgrId") String deviceManagerId, @PathParam("propId") String propertyId, FullProperty property) throws Exception {
        redhawkManager.setProperty(property, nameServer, "devicemanager", domainName + "/" + deviceManagerId);
        return Response.ok().build();
    }
}
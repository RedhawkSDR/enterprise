package redhawk.rest.endpoints;

import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.rest.exceptions.ResourceNotFound;
import redhawk.rest.model.DeviceContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path("/{nameserver}/domains/{domain}/devicemanagers/{devmanager}/devices")
public class RedhawkDeviceResource extends RedhawkBaseResource {

    private static Logger logger = Logger.getLogger(RedhawkDeviceResource.class.getName());

    @PathParam("nameserver")
    private String nameServer;

    @PathParam("domain")
    private String domainName;

    @PathParam("devmanager")
    private String devManagerName;

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDevices(@QueryParam("fetch") @DefaultValue("EAGER") FetchMode fetchMode) throws ResourceNotFound, Exception {
        return Response.ok(new DeviceContainer(redhawkManager.getAll(nameServer, "device", domainName + "/" + devManagerName, fetchMode))).build();
    }

    @GET
    @Path("/{deviceId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDevice(@PathParam("deviceId") String deviceId) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.get(nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId)).build();
    }

    @GET
    @Path("/{deviceId}/properties")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDeviceProperties(@PathParam("deviceId") String deviceId) throws ResourceNotFound, ResourceNotFoundException, Exception {
        return Response.ok(redhawkManager.getProperties(nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId)).build();
    }

    @GET
    @Path("/{deviceId}/properties/{propId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDeviceProperty(@PathParam("deviceId") String deviceId, @PathParam("propId") String propertyId) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.getProperty(propertyId, nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId)).build();
    }

    @POST
    @Path("/{deviceId}/properties")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response setDeviceProperties(@PathParam("deviceId") String deviceId, List<FullProperty> properties) throws Exception {
        redhawkManager.setProperties(properties, nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId);
        return Response.ok().build();
    }

    @PUT
    @Path("/{deviceId}/properties/{propId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response setDeviceProperty(@PathParam("deviceId") String deviceId, @PathParam("propId") String propertyId, FullProperty property) throws Exception {
        redhawkManager.setProperty(property, nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId);
        return Response.ok().build();
    }
}
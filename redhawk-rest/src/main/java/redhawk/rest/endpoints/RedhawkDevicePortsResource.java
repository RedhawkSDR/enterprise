package redhawk.rest.endpoints;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import redhawk.rest.exceptions.ResourceNotFound;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.PortContainer;

@Path("/{nameserver}/domains/{domain}/devicemanagers/{devmanager}/devices/{deviceId}/ports")
@Api(value="/{nameserver}/domains/{domain}/devicemanagers/{devmanager}/devices/{deviceId}/ports")
public class RedhawkDevicePortsResource extends RedhawkBaseResource{
    private static Logger logger = Logger.getLogger(RedhawkDevicePortsResource.class.getName());

    @PathParam("nameserver")
    private String nameServer;

    @PathParam("domain")
    private String domainName;

    @PathParam("devmanager")
    private String devManagerName;
    
    @PathParam("deviceId")
    private String deviceId;
    
    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Returns all ports for a device"
    		)
    public Response getPorts() throws ResourceNotFound, Exception {
        return Response.ok(new PortContainer(redhawkManager.getAll(nameServer, "deviceport", domainName + "/" + devManagerName + "/" + deviceId, FetchMode.EAGER))).build();
    }

    @GET
    @Path("/{portId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Returns a specific port for a device"
    		)
    public Response getPort(@PathParam("portId") String portName) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.get(nameServer, "deviceport", domainName + "/" + devManagerName + "/" + deviceId + "/" + portName)).build();
    }

    @GET
    @Path("/{portId}/statistics")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Returns port statistics for a device"
    		)
    public Response getPortStatistics(@PathParam("portId") String portName) throws Exception {
        return Response.ok(redhawkManager.getRhPortStatistics(nameServer, "deviceport", domainName + "/" + devManagerName + "/" + deviceId + "/" + portName)).build();
    }
}

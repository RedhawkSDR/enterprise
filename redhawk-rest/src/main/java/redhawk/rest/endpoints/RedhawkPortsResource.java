package redhawk.rest.endpoints;

import redhawk.rest.RedhawkManager;
import redhawk.rest.exceptions.ResourceNotFound;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.PortContainer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/{nameserver}/domains/{domain}/applications/{applicationId}/components/{componentId}/ports")
public class RedhawkPortsResource extends RedhawkBaseResource {

    private static Logger logger = Logger.getLogger(RedhawkPortsResource.class.getName());

    private RedhawkManager redhawkManager;

    @PathParam("nameserver")
    private String nameServer;

    @PathParam("domain")
    private String domainName;

    @PathParam("applicationId")
    private String applicationId;

    @PathParam("componentId")
    private String componentId;

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getPorts() throws ResourceNotFound, Exception {
        return Response.ok(new PortContainer(redhawkManager.getAll(nameServer, "port", domainName + "/" + applicationId + "/" + componentId, FetchMode.EAGER))).build();
    }

    @GET
    @Path("/{portId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getPort(@PathParam("portId") String portName) throws ResourceNotFound, Exception {
        return Response.ok(redhawkManager.get(nameServer, "port", domainName + "/" + applicationId + "/" + componentId + "/" + portName)).build();
    }

    @GET
    @Path("/{portId}/statistics")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getPortStatistics(@PathParam("portId") String portName) throws Exception {
        return Response.ok(redhawkManager.getRhPortStatistics(nameServer, "port", domainName + "/" + applicationId + "/" + componentId + "/" + portName)).build();
    }

    public RedhawkManager getRedhawkManager() {
        return redhawkManager;
    }

    public void setRedhawkManager(RedhawkManager redhawkManager) {
        this.redhawkManager = redhawkManager;
    }


}
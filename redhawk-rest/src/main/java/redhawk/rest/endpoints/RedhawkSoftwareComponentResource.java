package redhawk.rest.endpoints;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import redhawk.rest.RedhawkManager;
import redhawk.rest.exceptions.ResourceNotFound;

@Path("/{nameserver}/domains/{domain}/applications/{applicationId}/components/{componentId}/softwarecomponent")
public class RedhawkSoftwareComponentResource extends RedhawkBaseResource{

    private static Logger logger = Logger.getLogger(RedhawkSoftwareComponentResource.class.getName());


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
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getSoftwareComponent() throws ResourceNotFound, Exception {
		return Response.ok(redhawkManager.get(nameServer,
						"softwarecomponent", domainName + "/" + applicationId
								+ "/" + componentId)).build();
	}

	public RedhawkManager getRedhawkManager() {
		return redhawkManager;
	}

	public void setRedhawkManager(RedhawkManager redhawkManager) {
		this.redhawkManager = redhawkManager;
	}
}

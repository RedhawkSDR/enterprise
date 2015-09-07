package redhawk.rest.endpoints;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import redhawk.driver.xml.model.sca.sad.Softwareassembly;
import redhawk.rest.exceptions.ResourceNotFound;
import redhawk.rest.model.WaveformContainer;
import redhawk.rest.model.WaveformInfo;

@Path("/{nameserver}/domains/{domain}/waveforms")
public class RedhawkWaveformResource extends RedhawkBaseResource {

    private static Logger logger = Logger.getLogger(RedhawkWaveformResource.class.getName());

  @PathParam("nameserver")
  private String nameServer;
  
  @PathParam("domain")
  private String domainName;
  
  
	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getApplications() throws ResourceNotFound, Exception{
		Map<String, Softwareassembly> applications = redhawkManager.getWaveforms(nameServer, domainName);

		List<WaveformInfo> waveforms = new ArrayList<WaveformInfo>();
		
		applications.forEach((k,v) -> {
			WaveformInfo info = new WaveformInfo();
			info.setSadLocation(k);
			info.setName(v.getName());
			info.setId(v.getId());
			waveforms.add(info);
		});
		
		
		return Response.ok(new WaveformContainer(waveforms)).build();
	}
	
	@GET
	@Path("/{waveformId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getApplications(@PathParam("waveformId") String waveformId) throws ResourceNotFound, Exception{
		Map<String, Softwareassembly> applications = redhawkManager.getWaveforms(nameServer, domainName);
		
		Optional<Softwareassembly> assembly = applications.values().stream().filter(v -> {
			if(v.getId().equalsIgnoreCase(waveformId)){
				return true;
			}
			return false;
		}).findFirst();
		
		if(assembly.isPresent()){
			return Response.ok(assembly.get()).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
		
	}  
}
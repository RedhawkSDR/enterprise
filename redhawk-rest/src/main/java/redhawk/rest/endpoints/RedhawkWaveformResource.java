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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;
import redhawk.rest.exceptions.ResourceNotFound;
import redhawk.rest.model.WaveformContainer;
import redhawk.rest.model.WaveformInfo;

@Path("/{nameserver}/domains/{domain}/waveforms")
@Api("/{nameserver}/domains/{domain}/waveforms")
public class RedhawkWaveformResource extends RedhawkBaseResource {

	private static Logger logger = Logger.getLogger(RedhawkWaveformResource.class.getName());

	@PathParam("nameserver")
	private String nameServer;

	@PathParam("domain")
	private String domainName;

	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(
    		value="Returns Waveforms Available in Domain"
    		)	
	public Response getApplications() throws ResourceNotFound, Exception {
		Map<String, Softwareassembly> applications = redhawkManager.getWaveforms(nameServer, domainName);

		List<WaveformInfo> waveforms = new ArrayList<WaveformInfo>();

		applications.forEach((k, v) -> {
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
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(
    		value="Returns a Specific Waveform in a Domain"
    		)		
	public Response getApplications(@PathParam("waveformId") String waveformId) throws ResourceNotFound, Exception {
		Map<String, Softwareassembly> applications = redhawkManager.getWaveforms(nameServer, domainName);

		Optional<Softwareassembly> assembly = applications.values().stream().filter(v -> {
			if (v.getId().equalsIgnoreCase(waveformId)) {
				return true;
			}
			return false;
		}).findFirst();

		if (assembly.isPresent()) {
			return Response.ok(assembly.get()).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}

	}
}

package redhawk.rest.endpoints;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import redhawk.driver.xml.model.sca.sad.Softwareassembly;
import redhawk.rest.model.WaveformContainer;
import redhawk.rest.model.WaveformInfo;

public class RedhawkWaveformResourceIT extends RedhawkResourceTestBase{
	@Test
	public void testGetWaveforms() throws InterruptedException{
		WebTarget target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/waveforms");
		//System.out.println(target.request().get().readEntity(String.class));
		Response response = target.request().accept(MediaType.APPLICATION_XML).get();
		
		WaveformContainer wvContainer = response.readEntity(WaveformContainer.class);
		assertEquals(200, response.getStatus());		
		
		for(WaveformInfo waveform : wvContainer.getDomains()){
			target = client.target(baseUri+"localhost:2809/domains/"+domainName+"/waveforms/"+waveform.getId());
			response = target.request().accept(MediaType.APPLICATION_XML).get();
			Softwareassembly info = response.readEntity(Softwareassembly.class);
			assertEquals(200, response.getStatus());
		}
	}
}

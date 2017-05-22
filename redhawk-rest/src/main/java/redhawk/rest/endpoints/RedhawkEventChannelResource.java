package redhawk.rest.endpoints;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.rest.model.EventChannel;
import redhawk.rest.model.EventChannelContainer;
import redhawk.rest.model.FetchMode;

@Path("/{nameserver}/domains/{domain}/eventchannels")
@Api(value="/{nameserver}/domains/{domain}/eventchannels")
public class RedhawkEventChannelResource extends RedhawkBaseResource{
	private static Logger logger = Logger.getLogger(RedhawkEventChannelResource.class.getName());
		
    @ApiParam(value = "url for your name server")
    @PathParam("nameserver")
    private String nameServer;

    @ApiParam(value = "Name of REDHAWK Domain")
    @PathParam("domain")
    private String domainName;
    
    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    	value="GET REDHAWK Event Channels"
    )
    public EventChannelContainer getEventChannels() throws ResourceNotFoundException, Exception{
    	return new EventChannelContainer(redhawkManager.getAll(nameServer, "eventchannel", domainName, FetchMode.EAGER));
    }
    
    @GET
    @Path("/{eventchannel}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Event Channel"
    		)
    public EventChannel getEventChannel(@PathParam("eventchannel") String eventChannel) throws ResourceNotFoundException, Exception{
    	return redhawkManager.get(nameServer, "eventchannel", domainName, eventChannel);
    }
    
    
}

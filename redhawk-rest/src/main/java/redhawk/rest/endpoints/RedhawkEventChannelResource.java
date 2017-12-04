package redhawk.rest.endpoints;

import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.rest.model.EventChannel;
import redhawk.rest.model.EventChannelContainer;
import redhawk.rest.model.FetchMode;

@Path("/{nameserver}/domains/{domain}/eventchannels")
@Api(value="eventchannels")
public class RedhawkEventChannelResource extends RedhawkBaseResource{
	private static Logger logger = Logger.getLogger(RedhawkEventChannelResource.class.getName());
	
    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    	value="GET REDHAWK Event Channels"
    )
    public EventChannelContainer getEventChannels(
    		@ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
     		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain) throws ResourceNotFoundException, Exception{
    	return new EventChannelContainer(redhawkManager.getAll(nameServer, "eventchannel", domain, FetchMode.EAGER));
    }
    
    @GET
    @Path("/{eventchannel}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Event Channel"
    		)
    public EventChannel getEventChannel(
    		@ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
     		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@PathParam("eventchannel") String eventChannel) throws ResourceNotFoundException, Exception{
    	return redhawkManager.get(nameServer, "eventchannel", domain, eventChannel);
    }
    
    @PUT
    @Path("/{eventchannel}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Create REDHAWK Event Channel"
    		)
    public Response createEventChannel(
    		@ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
     		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@PathParam("eventchannel") String eventChannel){
    	redhawkManager.createEventChannel(nameServer, domain, eventChannel);
    	
    	return Response.ok().build();
    }
    
    @DELETE
    @Path("/{eventchannel}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="DELETE REDHAWK Event Channel"
    		)
    public Response deleteEventChannel(
    		@ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
     		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@PathParam("eventchannel") String eventChannel){
    	redhawkManager.deleteEventChannel(nameServer, domain, eventChannel);
    	
    	return Response.ok().build();
    }
    
    @DELETE
    @Path("/{eventchannel}/registrant/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="DELETE REDHAWK Event Channel Registrant"
    		)
    public Response deleteEventChannelRegistrant(
    		@ApiParam(value = "url for your name server", required = true) @PathParam("nameserver") String nameServer,
     		@ApiParam(value = "Name of REDHAWK Domain", required = true) @PathParam("domain") String domain,
    		@PathParam("eventchannel") String eventChannel, @PathParam("id") String id){
    	redhawkManager.unsubscribeFromEventChannel(nameServer, domain, eventChannel, id);
    	
    	return Response.ok().build();
    }
}

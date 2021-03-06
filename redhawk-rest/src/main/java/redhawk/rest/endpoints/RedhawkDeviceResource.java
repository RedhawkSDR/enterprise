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

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import redhawk.driver.device.AdminState;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.rest.model.DeviceContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;
import redhawk.rest.model.TunerMode;

@Path("/{nameserver}/domains/{domain}/devicemanagers/{devmanager}/devices")
@Api(value="/{nameserver}/domains/{domain}/devicemanagers/{devmanager}/devices")
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
    @ApiOperation(
    		value="GET REDHAWK Devices"
    		)
    public Response getDevices(@QueryParam("fetch") @DefaultValue("EAGER") FetchMode fetchMode) throws Exception {
        return Response.ok(new DeviceContainer(redhawkManager.getAll(nameServer, "device", domainName + "/" + devManagerName, fetchMode))).build();
    }

    @GET
    @Path("/{deviceId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Device"
    		)
    public Response getDevice(@PathParam("deviceId") String deviceId) throws Exception {
        return Response.ok(redhawkManager.get(nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId)).build();
    }
    
    //TODO: Should be a PUT
    @POST
    @Path("/{deviceId}/allocate")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON})    
    @ApiOperation(
    		value="Allocate a REDHAWK Device"
    		)
    public Response allocateDevice(@PathParam("deviceId") String deviceId, @ApiParam(value="Map representing an allocation", required=true) Map<String, Object> allocation) throws Exception{
    	//TODO: Look into where the appropriate place to fix this is. 
    	redhawkManager.allocateDevice(nameServer, domainName+"/"+devManagerName+"/"+deviceId, allocation);
    	//TODO: Probably should return the successful tuner::status object
    	return Response.ok().build();
    }
    
    //TODO: Should be a DELETE
    @POST
    @Path("/{deviceId}/deallocate")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON})    
    @ApiOperation(
    		value="Deallocate a REDHAWK Device"
    		)
    public Response deallocateDevice(@PathParam("deviceId") String deviceId, @ApiParam(value="Allocation Id to delete", required=true) String allocationId) throws Exception{
    	redhawkManager.deallocateDevice(nameServer, domainName+"/"+devManagerName+"/"+deviceId, allocationId);
    	//TODO: Probably should return the successful tuner::status object
    	return Response.ok().build();
    }
    
    @GET
    @Path("/{deviceId}/tuners/{tunerMode}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Device by tuner mode"
    		)
    public Response tuners(@PathParam("deviceId") String deviceId, @PathParam("tunerMode") String tunerMode) throws Exception{
    	List<Map<String, Object>> tunerStatus = redhawkManager.getTuners(nameServer, domainName+"/"+devManagerName+"/"+deviceId, TunerMode.valueOf(tunerMode));

    	logger.info("Status is "+tunerStatus);
    	return Response.ok(tunerStatus).build();
    }
    
    @GET
    @Path("/{deviceId}/properties")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Device Properties"
    		)
    public Response getDeviceProperties(@PathParam("deviceId") String deviceId) throws Exception {
        return Response.ok(redhawkManager.getProperties(nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId)).build();
    }

    @GET
    @Path("/{deviceId}/properties/{propId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="GET REDHAWK Device Property"
    		)
    public Response getDeviceProperty(@PathParam("deviceId") String deviceId, @PathParam("propId") String propertyId) throws Exception {
        return Response.ok(redhawkManager.getProperty(propertyId, nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId)).build();
    }

    @POST
    @Path("/{deviceId}/properties")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Set REDHAWK Device Properties"
    		)
    public Response setDeviceProperties(@PathParam("deviceId") String deviceId, @ApiParam(value="List of properties to update", required=true) List<FullProperty> properties) throws Exception {
        redhawkManager.setProperties(properties, nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId);
        return Response.ok().build();
    }

    @PUT
    @Path("/{deviceId}/properties/{propId}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Set REDHAWK Device Property"
    		)
    public Response setDeviceProperty(@PathParam("deviceId") String deviceId, @PathParam("propId") String propertyId, @ApiParam(value="Property to update", required=true) FullProperty property) throws Exception {
        redhawkManager.setProperty(property, nameServer, "device", domainName + "/" + devManagerName + "/" + deviceId);
        return Response.ok().build();
    }
    
    @PUT
    @Path("/{deviceId}/adminstate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @ApiOperation(
    		value="Set REDHAWK Device AdminState"
    		)
    public Response setAdminState(@PathParam("deviceId") String deviceId, @ApiParam(value="AdminState for the Device(e.g. LOCKED/UNLOCKED)", required=true) String state) throws Exception {
    	AdminState stateObj = AdminState.valueOf(state);
    	
    	redhawkManager.setAdminState(nameServer, domainName+"/"+devManagerName+"/"+deviceId, stateObj);
    	//TODO: Probably should return the successful tuner::status object
    	return Response.ok().build();
    }
}


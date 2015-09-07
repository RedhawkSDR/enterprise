package redhawk.rest.endpoints;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import redhawk.rest.RedhawkManager;

public class RedhawkBaseResource {
	public RedhawkManager redhawkManager;

	
	@OPTIONS
	public Response getOptions() {
		return addCors(Response.ok()).build();
	}
	
	
	protected ResponseBuilder addCors(ResponseBuilder responseBuilder) {
		responseBuilder.header("Access-Control-Allow-Origin", "*");
		responseBuilder.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
		responseBuilder.header("Access-Control-Allow-Credentials", "true");
		responseBuilder.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		return responseBuilder;
	}
	
	
	public RedhawkManager getRedhawkManager() {
		return redhawkManager;
	}

	public void setRedhawkManager(RedhawkManager redhawkManager) {
		this.redhawkManager = redhawkManager;
	}
}

package redhawk.rest.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import redhawk.driver.exceptions.ResourceNotFoundException;

public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException>{

	@Override
	public Response toResponse(ResourceNotFoundException exception) {
		return Response.status(Status.NOT_FOUND).build();
	}

}

package redhawk.jaxrs.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.eclipse.jetty.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@PreMatching
public class CORSFilter implements ContainerRequestFilter{
	Logger logger = LoggerFactory.getLogger(CORSFilter.class);
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(requestContext.getMethod().equals(HttpMethod.OPTIONS)) {
		    logger.info("Making it into filter for Options");
			ResponseBuilder builder = new ResponseBuilderImpl();
					
					
			builder.status(200);		
	        builder.header("Access-Control-Allow-Origin", "*");
	        builder.header("Access-Control-Allow-Methods","GET, OPTIONS, HEAD, PUT, POST, DELETE");
	        builder.header("Access-Control-Allow-Headers","X-Requested-With,Content-Type,Accept,Origin,Authorization");
	        builder.header("Access-Control-Allow-Credentials", "true");
	        requestContext.abortWith(builder.build());
		}
	}

}

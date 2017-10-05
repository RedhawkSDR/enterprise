package redhawk.jaxrs.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redhawk.jaxrs.filter.util.RESTPermissionsReader;
import redhawk.jaxrs.filter.util.RestMethodAuthorizationMapper;

@Provider
@PreMatching
public class RoleBasedAuthorizationFilter implements ContainerRequestFilter {
	private Logger logger = LoggerFactory.getLogger(RoleBasedAuthorizationFilter.class.getName());

	private Map<String, RestMethodAuthorizationMapper> mapper = new HashMap<>();

	RESTPermissionsReader reader = new RESTPermissionsReader();

	private String restPermissionFileLocation;

	private Boolean doRoleBasedFilter = true;

	public RoleBasedAuthorizationFilter() {
		// TODO: Why doesn't this work??
		this.restPermissionFileLocation = System.getProperty("redhawk.jaxrs.permissions");
		logger.debug("Permissions file: " + this.restPermissionFileLocation);
		if (this.restPermissionFileLocation != null) {
			try {
				mapper = reader.readRestFile(this.restPermissionFileLocation);

				logger.info("Map: " + mapper);
				if (mapper != null) {
					for (Map.Entry<String, RestMethodAuthorizationMapper> entry : mapper.entrySet()) {
						logger.info("\t Key " + entry.getKey() + " Value: " + entry.getValue());
					}
				} else {
					logger.error("Mapper is null??? " + mapper.toString());
				}
			} catch (IOException | NullPointerException e) {
				logger.error("Unable to read rest permissions file. File will not be honored. " + e.getMessage());
				this.doRoleBasedFilter = false;
			}
		}else {
			logger.debug("No valid role based filter filter file will bypass filter");
			this.doRoleBasedFilter = false;
		}
	}

	public void filter(ContainerRequestContext requestContext) throws IOException {
		SecurityContext sc = requestContext.getSecurityContext();
		Principal principal = sc.getUserPrincipal();

		if(this.doRoleBasedFilter) {
			if (principal != null) {
				// TODO: Do absolute path as well
				RestMethodAuthorizationMapper heimdall = mapper.get(requestContext.getUriInfo().getPath());

				logger.info("===================================");
				logger.info("Principal: " + principal);
				logger.info("Is user in admin role: " + sc.isUserInRole("admin"));
				logger.info("Method: " + requestContext.getMethod());
				logger.info("Path: " + requestContext.getUriInfo().getPath());
				logger.info("Absolute Path: " + requestContext.getUriInfo().getPath());
				logger.info("===================================");
				Boolean permitted = heimdall.permitted(requestContext.getMethod(), sc);

				// Throw exception if user is not permitted
				if (!permitted)
					throw new WebApplicationException("User does not have appropriate permissions to access this endpoint",
							403);
			} else {
				throw new WebApplicationException("User principal required to access this endpoint", 403);
			}			
		}
	}
}
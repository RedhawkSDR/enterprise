package redhawk.jaxrs.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
	
	private Boolean strictPermissions = false;
	
	private final static String REST_PERMISSION_FILELOCATION = "redhawk.jaxrs.permissions.file";

	private final static String REST_PERMISSION_STRICT = "redhawk.jaxrs.permissions.strict";

	public RoleBasedAuthorizationFilter() {
		this(System.getProperty(REST_PERMISSION_FILELOCATION), Boolean.valueOf(System.getProperty(REST_PERMISSION_STRICT, "false")));
	}
	
	public RoleBasedAuthorizationFilter(String path) {
		this(path, Boolean.valueOf(System.getProperty(REST_PERMISSION_STRICT, "false")));
	}
	
	public RoleBasedAuthorizationFilter(String filterFile, Boolean strictPermissions) {
		this.restPermissionFileLocation = filterFile;
		this.strictPermissions = strictPermissions;
		//Setup allows access via Env/JNDI
		setup();
		logger.debug("Permissions file: " + this.restPermissionFileLocation);
		if (this.restPermissionFileLocation != null) {
			try {
				mapper = reader.readRestFile(this.restPermissionFileLocation, this.strictPermissions);

				logger.info("Map: " + mapper);
				if (mapper != null) {
					for (Map.Entry<String, RestMethodAuthorizationMapper> entry : mapper.entrySet()) {
						logger.debug("\t Key " + entry.getKey() + " Value: " + entry.getValue());
					}
				} else {
					logger.error("Mapper is null??? " + mapper.toString());
				}
			} catch (IOException | NullPointerException e) {
				logger.error("Unable to read rest permissions file. File will not be honored. " + e.getMessage());
				this.doRoleBasedFilter = false;
			}
		}else {
			logger.debug("No valid role based filter file will bypass filter");
			this.doRoleBasedFilter = false;
		}
	}
	
	public void setup() {
		try {
		    Context ctx = new InitialContext();
		    Context env = (Context) ctx.lookup("java:comp/env");
		    Object obj = env.lookup(REST_PERMISSION_FILELOCATION);
			if(obj!=null && this.restPermissionFileLocation==null) {
				logger.debug("Updates permission file location with information from web.xml");
				this.restPermissionFileLocation = obj.toString();
			}else if(obj!=null) {
				logger.debug("Found property but using System property");
			}
		} catch (NamingException e) {
			logger.warn("Unable to get properties via jndi env");
		}
	}

	public void filter(ContainerRequestContext requestContext) throws IOException {
		SecurityContext sc = requestContext.getSecurityContext();
		Principal principal = sc.getUserPrincipal();
		
		if(this.doRoleBasedFilter) {
			RestMethodAuthorizationMapper heimdall = mapper.get(getPathRoleMatchKey(requestContext.getUriInfo().getAbsolutePath().getPath()));
			if (principal != null && heimdall !=null) {

				logger.info("===================================");
				logger.info("Principal: " + principal);
				logger.info("Method: " + requestContext.getMethod());
				logger.info("Path: " + requestContext.getUriInfo().getAbsolutePath());
				logger.info("===================================");
				Boolean permitted = heimdall.permitted(requestContext.getMethod(), sc);

				// Throw exception if user is not permitted
				if (!permitted)
					throw new WebApplicationException("User does not have appropriate permissions to access this endpoint",
							403);
			}else if(heimdall==null) { 
				logger.warn("Path "+requestContext.getUriInfo().getPath()+" letting request through without role check. "
						+ "Update "+this.restPermissionFileLocation+" file if you'd like this path to be checked");
			}else {
				throw new WebApplicationException("User principal required to access this endpoint", 403);
			}			
		}
	}
	
	public String getPathRoleMatchKey(String incomingPath) {
		/*
		 * Find all matched keys by regex
		 */
		logger.debug("Path: "+incomingPath);
		Set<String> matchedKeys = mapper.keySet()
			.stream()
			.filter(entry -> incomingPath.matches(entry))
			.collect(Collectors.toSet());
	
		/*
		 * Take the longest length key out for the set
		 */
		logger.debug("Matched keys "+matchedKeys);
		String max = null; 
		if(!matchedKeys.isEmpty())
			max = Collections.max(matchedKeys, Comparator.comparing(s -> s.length()));
		
		return max;
	}
}
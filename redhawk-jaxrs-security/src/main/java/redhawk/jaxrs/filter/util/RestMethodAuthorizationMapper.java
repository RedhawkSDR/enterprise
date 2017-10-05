package redhawk.jaxrs.filter.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: Look into HttpMethodConstraintElement
public class RestMethodAuthorizationMapper {
	Logger logger = LoggerFactory.getLogger(RestMethodAuthorizationMapper.class.getName());
	
	/**
	 * Path security is being defined for
	 */
	private String path;
	
	/**
	 * Whether to be permissive if method is not defined in role map
	 */
	private Boolean strict = false;
	
	private final String ALL = "ALL";
	
	/**
	 * Map of methods and the roles that are available to access 
	 * that method. 
	 */
	private Map<String, Set<String>> methodToRole = new HashMap<>(); 

	public RestMethodAuthorizationMapper() {}
	
	public RestMethodAuthorizationMapper(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public void setMethodToRole(Map<String, Set<String>> methodToRole) {
		this.methodToRole = methodToRole;
	}
	
	public void addMethodToRole(String method, Set<String> roles) {
		if(methodToRole.containsKey(method)) {
			this.methodToRole.get(method).addAll(roles);
		}else {
			this.methodToRole.put(method, roles);
		}
	}
	
	/**
	 * Decides whether can access a REST 
	 * endpoint or not
	 * @param role
	 * @return
	 */
	public Boolean permitted(String method, SecurityContext sc) {
		//Check if role is contained in map
		//If not default to ALL
		if(!methodToRole.containsKey(method)) {
			method = ALL;
		}
			
		/*
		 * If roles are there check to see if passed in role has access. 
		 * If not and strict then return false. Else return true
		 */
		Set<String> roles = methodToRole.get(method);
		
		if(roles!=null) {
			for(String role : roles) {
				logger.info("Checking role: "+role);
				if(sc.isUserInRole(role))
					return true;
			}
			
			//User was not in role return false
			return false;
		}else if(strict) {
			return false;
		}else {
			return true; 
		}
	}
	
	@Override
	public String toString() {
		return "RestMethodAuthorizationMapper [path=" + path + ", strict=" + strict + ", methodToRole=" + methodToRole
				+ "]";
	}
}

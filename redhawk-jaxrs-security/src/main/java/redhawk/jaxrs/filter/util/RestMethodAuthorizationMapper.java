package redhawk.jaxrs.filter.util;

import java.util.HashMap;
import java.util.HashSet;
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
	private Map<String, Set<String>> methodToRoles = new HashMap<>(); 

	public RestMethodAuthorizationMapper() {}
	
	public RestMethodAuthorizationMapper(String path) {
		this.path = path;
	}
	
	public RestMethodAuthorizationMapper(String path, Boolean strict) {
		this.path = path;
		this.strict = strict;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public void setMethodToRole(Map<String, Set<String>> methodToRole) {
		this.methodToRoles = methodToRole;
	}
	
	public void addMethodToRole(String method, String role) {
		if(methodToRoles.containsKey(method)) {
			this.methodToRoles.get(method).add(role);
		}else {
			Set<String> roles = new HashSet<>();
			roles.add(role);
			this.methodToRoles.put(method, roles);
		}
	}
	
	public void addMethodToRole(String method, Set<String> roles) {
		if(methodToRoles.containsKey(method)) {
			this.methodToRoles.get(method).addAll(roles);
		}else {
			this.methodToRoles.put(method, roles);
		}
	}
	
	public Map<String, Set<String>> getMethodToRoles(){
		return methodToRoles;
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
		String origMethod = null; 
		if(!methodToRoles.containsKey(method)) {
			origMethod = method; 
			method = ALL;
		}
			
		/*
		 * If roles are there check to see if passed in role has access. 
		 * If not and strict then return false. Else return true
		 */
		Set<String> roles = methodToRoles.get(method);
		
		if(roles!=null) {
			for(String role : roles) {
				logger.debug("Checking role: "+role);
				//Be sure to trim in case user has spaces or something
				if(sc.isUserInRole(role.trim()))
					return true;
			}
			
			//User was not in role return false
			return false;
		}else if(strict) {
			return false;
		}else {
			logger.debug("No roles for this method: "+origMethod+" allowing user to access");
			return true; 
		}
	}
	
	public Boolean getStrict() {
		return strict;
	}

	public void setStrict(Boolean strict) {
		this.strict = strict;
	}
	
	@Override
	public String toString() {
		return "RestMethodAuthorizationMapper [path=" + path + ", strict=" + strict + ", methodToRole=" + methodToRoles
				+ "]";
	}
}

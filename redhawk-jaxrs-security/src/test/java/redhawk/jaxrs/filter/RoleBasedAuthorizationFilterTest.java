package redhawk.jaxrs.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;

public class RoleBasedAuthorizationFilterTest {
	@Test
	public void testRoleBasedAuthorizationNoFile() {
		RoleBasedAuthorizationFilter filter = new RoleBasedAuthorizationFilter(null);
		
		ContainerRequestContext rc = mock(ContainerRequestContext.class);
		SecurityContext sc = mock(SecurityContext.class);
		Principal principal = mock(Principal.class);
		
		when(rc.getSecurityContext()).thenReturn(sc);
		when(sc.getUserPrincipal()).thenReturn(principal);

		try {
			filter.filter(rc);
		}catch(IOException ex) {
			fail("No file set filter should just be skipped");
		}
	}
	
	@Test
	public void testRoleBaseAuthorizationBadFile() {
		RoleBasedAuthorizationFilter filter = new RoleBasedAuthorizationFilter("src/test/resources/gooGah");
		
		ContainerRequestContext rc = mock(ContainerRequestContext.class);
		SecurityContext sc = mock(SecurityContext.class);
		Principal principal = mock(Principal.class);
		
		when(rc.getSecurityContext()).thenReturn(sc);
		when(sc.getUserPrincipal()).thenReturn(principal);

		try {
			filter.filter(rc);
		}catch(IOException ex) {
			fail("No file set filter should just be skipped");
		}		
	}
	
	@Test
	public void testRoleBasedAuthorizationFilter() {
		RoleBasedAuthorizationFilter filter = new RoleBasedAuthorizationFilter("src/test/resources/rest-permissions.properties");
		
		ContainerRequestContext rc = mock(ContainerRequestContext.class);
		SecurityContext sc = mock(SecurityContext.class);
		Principal principal = mock(Principal.class);
		UriInfo uriInfo = mock(UriInfo.class);
		URI uri = mock(URI.class);
		
		/*
		 * Mock the returns for the expected use case
		 * user with admin permissions accessing /hello
		 */
		when(rc.getSecurityContext()).thenReturn(sc);
		when(uriInfo.getAbsolutePath()).thenReturn(uri);
		when(uri.getPath()).thenReturn("/hello");
		when(sc.getUserPrincipal()).thenReturn(principal);
		when(sc.isUserInRole("admin")).thenReturn(true);
		when(rc.getUriInfo()).thenReturn(uriInfo);
		when(rc.getMethod()).thenReturn("GET");
		
		try {
			filter.filter(rc);
		} catch (IOException e) {
			fail("No exceptions should occues user should have permissions");
		}
		
		/*
		 * Mock use case where user tries to hit hello/world 
		 * logged in as guest
		 */
		when(uri.getPath()).thenReturn("/hello/world");
		when(sc.isUserInRole("admin")).thenReturn(false);
		try {
			filter.filter(rc);
			fail("An exception should been thrown");
		} catch (WebApplicationException | IOException e) {
			assertTrue("Appropriate exception has been thrown", true);
		}
	}
	
	@Test
	public void testRoleBasedAuthorizationFilterRegex() {
		RoleBasedAuthorizationFilter filter = new RoleBasedAuthorizationFilter("src/test/resources/redhawk-rest-permissions.properties");
		
		ContainerRequestContext rc = mock(ContainerRequestContext.class);
		SecurityContext sc = mock(SecurityContext.class);
		Principal principal = mock(Principal.class);
		UriInfo uriInfo = mock(UriInfo.class);
		URI uri = mock(URI.class);
		
		/*
		 * Mock the returns for the expected use case
		 * user with admin permissions accessing /redhawk.* 
		 * as a GET request
		 */
		when(rc.getSecurityContext()).thenReturn(sc);
		when(uriInfo.getAbsolutePath()).thenReturn(uri);
		when(uri.getPath()).thenReturn("/rest/redhawk/localhost:2809/domains");
		when(sc.getUserPrincipal()).thenReturn(principal);
		when(sc.isUserInRole("admin")).thenReturn(true);
		when(rc.getUriInfo()).thenReturn(uriInfo);
		when(rc.getMethod()).thenReturn("GET");
		
		try {
			filter.filter(rc);
			
			//Make sure user with admin can do POST/PUT/DELETE
			when(rc.getMethod()).thenReturn("POST");
			filter.filter(rc);
			
			when(rc.getMethod()).thenReturn("DELETE");
			filter.filter(rc);
			
			when(rc.getMethod()).thenReturn("PUT");
			filter.filter(rc);
		} catch (IOException e) {
			fail("No exceptions should occues user should have permissions");
		}
		
		//Now test user without admin accessing POST/PUT/DELETE
		when(sc.isUserInRole("admin")).thenReturn(false);
		try {
			//Left off at PUT
			filter.filter(rc);
			fail("An exception should been thrown");
		} catch (WebApplicationException | IOException e) {
			assertTrue("Appropriate exception has been thrown", true);
			
			try {
				//Check POST
				when(rc.getMethod()).thenReturn("POST");
				filter.filter(rc);
			} catch (WebApplicationException | IOException e1) {
				assertTrue("Appropriate exception has been thrown", true);
				
				try {
					//Check DELETE
					when(rc.getMethod()).thenReturn("DELETE");
					filter.filter(rc);
				} catch (WebApplicationException | IOException e2) {
					assertTrue("Appropriate exception has been thrown", true);
				}
			}
		}	
	}
	
	@Test
	public void testRoleBasedAuthorizationFilterRegexComplex() {
		RoleBasedAuthorizationFilter filter = new RoleBasedAuthorizationFilter("src/test/resources/customredhawk-rest-permissions.properties");
		
		ContainerRequestContext rc = mock(ContainerRequestContext.class);
		SecurityContext sc = mock(SecurityContext.class);
		Principal principal = mock(Principal.class);
		UriInfo uriInfo = mock(UriInfo.class);
		URI uri = mock(URI.class);

		
		/*
		 * Mock the returns for the expected use case
		 * user with admin permissions accessing /redhawk.* 
		 * as a GET request
		 */
		when(rc.getSecurityContext()).thenReturn(sc);
		when(uriInfo.getAbsolutePath()).thenReturn(uri);
		when(uri.getPath()).thenReturn("/rest/redhawk/localhost:2809/domains");
		when(sc.getUserPrincipal()).thenReturn(principal);
		when(sc.isUserInRole("admin")).thenReturn(true);
		when(rc.getUriInfo()).thenReturn(uriInfo);
				
		try {
			//Make sure this is matching the appropriate entry in props file
			assertEquals("/rest/redhawk.*", filter.getPathRoleMatchKey("/rest/redhawk/localhost:2809/domains"));
			when(rc.getMethod()).thenReturn("GET");
			filter.filter(rc);
			
			//Make sure user with admin can do POST/PUT/DELETE
			when(rc.getMethod()).thenReturn("POST");
			filter.filter(rc);
			
			when(rc.getMethod()).thenReturn("DELETE");
			filter.filter(rc);
			
			when(rc.getMethod()).thenReturn("PUT");
			filter.filter(rc);
		} catch (IOException e) {
			fail("No exceptions should occues user should have permissions");
		}
		
		/*
		 * Now mock a user accessing /redhawk/customhost:2809/domains
		 */
		when(uri.getPath()).thenReturn("/rest/redhawk/customhost:2809/domains");
		
		try {
			//Make sure this is matching the appropriate entry in props file
			assertEquals("/rest/redhawk/customhost:2809.*", filter.getPathRoleMatchKey("/rest/redhawk/customhost:2809/domains"));
			when(rc.getMethod()).thenReturn("GET");
			filter.filter(rc);
			
			//Make sure user with admin can do POST/PUT/DELETE
			when(rc.getMethod()).thenReturn("POST");
			filter.filter(rc);
			
			when(rc.getMethod()).thenReturn("DELETE");
			filter.filter(rc);
			
			when(rc.getMethod()).thenReturn("PUT");
			filter.filter(rc);
		} catch (IOException e) {
			fail("No exceptions should occues user should have permissions");
		}
		
		/*
		 * Test user not being in admin role 
		 */
		try {
			when(sc.isUserInRole("admin")).thenReturn(false);
			filter.filter(rc);
			fail("An exception should been thrown");
		}catch(WebApplicationException | IOException e) {
			assertTrue("Appropriate exception has been thrown", true);
		}
		
		/*
		 * Now mock a user accessing /redhawk/customhost:2809/domains/SpecialSnowFlake
		 */
		when(uri.getPath()).thenReturn("/rest/redhawk/customhost:2809/domains/SpecialSnowFlake");

		try {
			when(sc.isUserInRole("special")).thenReturn(false);
			filter.filter(rc);
			fail("An exception should been thrown");
		}catch(WebApplicationException | IOException e) {
			assertTrue("Appropriate exception has been thrown", true);
		}
		
		/*
		 * Give user access to endpoint by putting them in role
		 */
		when(sc.isUserInRole("special")).thenReturn(true);

		try {
			//Make sure this is matching the appropriate entry in props file
			assertEquals("/rest/redhawk/customhost:2809/domains/SpecialSnowFlake.*", filter.getPathRoleMatchKey("/rest/redhawk/customhost:2809/domains/SpecialSnowFlake"));
			when(rc.getMethod()).thenReturn("GET");
			filter.filter(rc);
			
			//Make sure user with admin can do POST/PUT/DELETE
			when(rc.getMethod()).thenReturn("POST");
			filter.filter(rc);
			
			when(rc.getMethod()).thenReturn("DELETE");
			filter.filter(rc);
			
			when(rc.getMethod()).thenReturn("PUT");
			filter.filter(rc);
		} catch (IOException e) {
			fail("No exceptions should occues user should have permissions");
		}
	}
}

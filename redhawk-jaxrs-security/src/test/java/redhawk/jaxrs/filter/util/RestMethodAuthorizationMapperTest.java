package redhawk.jaxrs.filter.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.core.SecurityContext;

import org.junit.Test;

public class RestMethodAuthorizationMapperTest {
	@Test
	public void testRESTMethodAuthorizationMapper() {
		RestMethodAuthorizationMapper mapper = new RestMethodAuthorizationMapper("hello");
		
		String[] roles = new String[] {"redhawk, guest"};
		mapper.addMethodToRole("GET", Arrays.stream(roles).collect(Collectors.toSet()));
		mapper.addMethodToRole("POST", "redhawk");
		
		Map<String, Set<String>> expectedMethodToRoles = new HashMap<>();
		expectedMethodToRoles.put("GET", Arrays.stream(roles).collect(Collectors.toSet()));
		expectedMethodToRoles.put("POST", Collections.singleton("redhawk"));
		
		assertEquals(expectedMethodToRoles, mapper.getMethodToRoles());
	}
	
	@Test
	public void testPermitted() {
		SecurityContext sc = mock(SecurityContext.class);
		
		//On the endpoint being tested user redhawk will have appropriate role 
		when(sc.isUserInRole("admin")).thenReturn(true);
		
		RestMethodAuthorizationMapper mapper = new RestMethodAuthorizationMapper("foo");
		mapper.addMethodToRole("GET", "admin");
		
		assertTrue(mapper.permitted("GET", sc));
		
		//Now test that a user that doesn't have admin can't access this endpoint 
		when(sc.isUserInRole("admin")).thenReturn(false);
		assertTrue(!mapper.permitted("GET", sc));
	}
	
	@Test
	public void testPermittedUndefinedRolesForMethod() {
		SecurityContext sc = mock(SecurityContext.class);
		
		//All users can access any method on this endpoint
		RestMethodAuthorizationMapper mapper = new RestMethodAuthorizationMapper("foo");
		mapper.addMethodToRole("POST", "admin");
		
		assertTrue(mapper.permitted("GET", sc));
		
		//Now make this strict and make sure user can not access
		mapper.setStrict(true);
		assertTrue(!mapper.permitted("GET", sc));
	}
}

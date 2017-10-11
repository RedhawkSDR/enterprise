package redhawk.jaxrs.filter.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RESTPermissionReaderTest {
	private Logger logger = LoggerFactory.getLogger(RESTPermissionReaderTest.class);
	
	private RESTPermissionsReader reader = new RESTPermissionsReader(); 
	
	@Test
	public void testReadPermFile() {		
		try {
			Map<String, RestMethodAuthorizationMapper> test = reader.readRestFile("src/test/resources/rest-permissions.csv", false);
		
			//Check for expected values
			assertEquals("Should be two keys", 2, test.size());
			
			String[] expectedPathKeys = new String[] {"/hello", "/hello/world"};
			assertEquals(test.keySet(), Arrays.stream(expectedPathKeys).collect(Collectors.toSet()));
		
			//Check mapper values are correct
			for(RestMethodAuthorizationMapper value : test.values()) {
				if(value.getPath().equals("/hello")) {
					//Roles should not be null
					Set<String> roles = value.getMethodToRoles().get("GET");
					assertNotNull(roles);
					
					//Should be two roles for this
					assertEquals(2, roles.size());
					
					//Expected roles
					String[] expectedRoles = new String[] {"guest", "admin"};
					assertEquals(roles, Arrays.stream(expectedRoles).collect(Collectors.toSet()));
				}else if(value.getPath().equals("/hello/world")) {
					//Roles should not be null
					Set<String> roles = value.getMethodToRoles().get("ALL");
					assertNotNull(roles);

					//Should be 1 role for this 
					assertEquals(1, roles.size());
					
					//Expected roles
					String[] expectedRoles = new String[] {"admin"};
					assertEquals(roles, Arrays.stream(expectedRoles).collect(Collectors.toSet()));
				}else {
					fail("Unexpected path no expected values "+value);
				}
			}
		} catch (IOException e) {
			fail("File should exist!!!"+e.getMessage());
		}
	}
	
	@Test
	public void testAdminOnly() {
		try {
			Map<String, RestMethodAuthorizationMapper> test = reader.readRestFile("src/test/resources/admin-only.properties", false);
			
			for(Map.Entry<String, RestMethodAuthorizationMapper> entry : test.entrySet()) {
				logger.info("Key: "+entry.getKey()+" Value: "+entry.getValue());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

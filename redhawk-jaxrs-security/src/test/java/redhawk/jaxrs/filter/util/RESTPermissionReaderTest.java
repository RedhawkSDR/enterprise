package redhawk.jaxrs.filter.util;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

public class RESTPermissionReaderTest {
	@Test
	public void testReadPermFile() {
		RESTPermissionsReader reader = new RESTPermissionsReader();
		
		try {
			Map<String, RestMethodAuthorizationMapper> test = reader.readRestFile("src/test/resources/rest-permissions.csv");
		
			for(Map.Entry<String, RestMethodAuthorizationMapper> entry : test.entrySet()) {
				System.out.println("Key: "+entry.getKey()+" Value: "+entry.getValue());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

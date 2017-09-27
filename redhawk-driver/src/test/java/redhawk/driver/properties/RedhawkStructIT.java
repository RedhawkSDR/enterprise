package redhawk.driver.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.Any;
import org.ossie.properties.AnyUtils;

import CF.DataType;
import CF.PropertySet;
import CF.PropertySetHelper;
import CF.PropertySetPackage.InvalidConfiguration;
import CF.PropertySetPackage.PartialConfiguration;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkStructIT extends RedhawkPropertyTestBase{
	
	@Test
	public void testSetStructDomain() throws Exception {
		try {		
			RedhawkStruct structProp = manager.getProperty("client_wait_times");
			Random rand = new Random();
			Integer devices = rand.nextInt(100000)+1000;
			Integer services = rand.nextInt(100000)+1000;
			Integer managers = rand.nextInt(100000)+1000;
			
			Map<String, Object> struct = new HashMap<>();
			struct.put("client_wait_times::devices", new Long(devices));
			struct.put("client_wait_times::services", new Long(services));
			struct.put("client_wait_times::managers", new Long(managers));
			
			structProp.setValue(struct);
				
			Map<String, Object> value = structProp.getValue();
			
			//These should be equal
			assertEquals(struct, value);
			
			structProp.setValue("client_wait_times::devices", 87000l);
			
			//Test getting one elemement from the struct
			assertEquals(new Long(87000), structProp.getValue("client_wait_times::devices"));
		} catch (MultipleResourceException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSetStructApplication() {
		RedhawkStruct structProp = allPropsWaveform.getProperty("cartoon_character");
		Map<String, Object> struct = new HashMap<>();
		struct.put("age", 12);
		struct.put("friend", "Mr. Krabs");
		
		try {
			structProp.setValue(struct);
			
			Map<String, Object> value = structProp.getValue();
			assertEquals(new Double(12), value.get("age"));
			assertEquals("Mr. Krabs", value.get("friend"));
			
			//Make sure setValue w/ prop works
			structProp.setValue("age", 30);
			value = structProp.getValue();
			assertEquals(new Double(30), value.get("age"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

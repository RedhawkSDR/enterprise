package redhawk.driver.properties;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Map;

import org.junit.Test;

public class RedhawkSimpleIT extends RedhawkPropertyTestBase{
	@Test
	public void testRedhawkSimple() throws Exception {
		for(Map.Entry<String, RedhawkProperty> prop : allPropsWaveform.getProperties().entrySet()) {
			System.out.println("key: "+prop.getKey());
			System.out.println("\t "+prop.getValue());
		}
		
		/*
		 * Set a char value 
		 */
		RedhawkSimple simple = allPropsWaveform.getProperty("simple_char");
		
		assertEquals('a', simple.getValue());
		
		//Update value
		simple.setValue('b');
		assertEquals('b', simple.getValue());
		
		//Make sure cached value was updated post set only need to do this once 
		//Since all the other methods are going through the same code
		assertEquals('b', simple.getValue(false));
		
		/*
		 * Set a double property value 
		 */
		simple = allPropsWaveform.getProperty("simple_double");
		
		assertEquals(2.0, simple.getValue());
		simple.setValue(3.0);
		assertEquals(3.0, simple.getValue());
		
		/*
		 * Set a String property value 
		 */
		simple = allPropsWaveform.getProperty("simple_string");
		
		assertEquals("World", simple.getValue());
		simple.setValue("Hello");
		assertEquals("Hello", simple.getValue());
		
		/*
		 * Set a float property value 
		 */
		simple = allPropsWaveform.getProperty("simple_float");
		
		assertEquals(new Float(1.0),simple.getValue());
		//Great user doesn't care Float/Double/Integer just convert to the right type 
		//Please and thank you lol
		simple.setValue(10);
		assertEquals(new Float(10), simple.getValue());
		
		/*
		 * Set a Long property value 
		 */
		simple = allPropsWaveform.getProperty("simple_long");
		
		//Long is down converted to an int by Any implementation
		assertEquals(1, simple.getValue());
		simple.setValue(1200);
		assertEquals(1200, simple.getValue());
		
		/*
		 * Set a Long Long property value 
		 */
		simple = allPropsWaveform.getProperty("simple_longlong");

		assertEquals(new Long(20000), simple.getValue());
		simple.setValue(70000);
		assertEquals(new Long(70000), simple.getValue());
		
		/*
		 * Set a ulong property value 
		 */
		simple = allPropsWaveform.getProperty("simple_ulong");

		assertEquals(new Long(200), simple.getValue());
		simple.setValue(12000);
		assertEquals(new Long(12000), simple.getValue());
		
		/*
		 * Set a simple ulonglong
		 */
		simple = allPropsWaveform.getProperty("simple_ulonglong");

		BigInteger bigInt = BigInteger.valueOf(127);
		assertEquals(bigInt, simple.getValue());
		simple.setValue(12000);
		bigInt = BigInteger.valueOf(12000);
		assertEquals(bigInt, simple.getValue());
	}
}

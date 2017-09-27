package redhawk.driver.properties;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RedhawkSimpleSequenceIT extends RedhawkPropertyTestBase{
	RedhawkSimpleSequence seq;

	@Before
	public void setupRedhawkSimpleSequenceIT() throws Exception {
		seq = component.getProperty("examples");
		
		Object[] expectedValue = new Object[] {"Foo", "Bar", "Hello", "World"};
		seq.setValue(expectedValue);
	}
	
	@Test
	public void testSimpleSequence() throws Exception {
		List<Object> value = seq.getValue();
		Object[] expectedValue = new Object[] {"Foo", "Bar", "Hello", "World"};
		
		//Make sure you can get expected defaults
		assertArrayEquals(expectedValue, value.toArray());
		
		String[] simpleSequence = new String[] {"please", "excuse", "my", "dear", "aunt", "sally"};
		seq.setValue(simpleSequence);
		
		//Check to see if you can get value
		value = seq.getValue(false);
		assertArrayEquals(simpleSequence, value.toArray());
		
		value = seq.getValue();
		assertArrayEquals(simpleSequence, value.toArray());
	}
	
	@Test
	public void testSimpleSequenceHelpers() throws Exception {		
		List<Object> value = seq.getValue();
		
		//Check size 
		assertEquals(4, value.size());
	
		//Test clearing all values 
		seq.clearAllValues();
		value = seq.getValue();
		assertEquals(0, value.size());
		
		//Add value 
		seq.addValue("Hello");
		value = seq.getValue();
		assertEquals(1, value.size());		
		
		//Remove value 
		seq.removeValue("Hello");
		value = seq.getValue();
		assertEquals(0, value.size());
		
		//Add values
		value = new ArrayList<>();
		value.add("Foo");
		value.add("bar");
		seq.addValues(value.toArray());
		value = seq.getValue();
		assertEquals(2, value.size());
	}
}

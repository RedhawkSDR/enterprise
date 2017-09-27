package redhawk.driver.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.xml.model.sca.prf.StructSequence;

public class RedhawkStructSequenceIT extends RedhawkPropertyTestBase{
	static List<Map<String, Object>> defaultValue; 
	static RedhawkStructSequence structSeq;
	
	@BeforeClass
	public static void setupDefaultsRedhawkStructSequenceIT() {
		structSeq = allPropsWaveform.getProperty("main_characters");
		defaultValue = structSeq.getValue();
	}
	
	@Before
	public void setupRedhawkStructSequenceIT() throws Exception {
		//Making sure values get reset after each test
		structSeq = allPropsWaveform.getProperty("main_characters");
		structSeq.setValue(defaultValue);
	
		 List<Map<String, Object>> value = structSeq.getValue();
		assertEquals(defaultValue.size(), value.size());
	}
	
	@Test
	public void testGetAndSetStructSequence() throws Exception {
		structSeq = allPropsWaveform.getProperty("main_characters");
		List<Map<String, Object>> value = structSeq.getValue();
				
		//Should be three values in here
		assertEquals(defaultValue.size(), value.size());
		
		List<Map<String, Object>> newValues = new ArrayList<>(); 
		
		Map<String, Object> obj = new HashMap<>();
		obj.put("actor_name", "Johnny Depp");
		obj.put("actor_country", "Clifford");

		newValues.add(obj);
		
		structSeq.setValue(newValues);
		assertEquals(newValues, structSeq.getValue());
	}
	
	@Test
	public void testHelperMethods() throws Exception {
		structSeq = allPropsWaveform.getProperty("main_characters");

		Map<String, Object> obj = new HashMap<>();
		obj.put("actor_name", "Johnny Depp");
		obj.put("actor_country", "Clifford");
		
		//Add Item to sequence
		structSeq.addStructToSequence(obj);
		
		List<Map<String, Object>> value = structSeq.getValue();
		
		//Should now be 4 items 
		assertEquals(4, value.size());
		
		//See if you can remove all
		structSeq.removeAllStructs();
		
		value = structSeq.getValue();
		assertEquals(0, value.size());
		
		//Put defaults back with addStructs
		structSeq.addStructsToSequence(defaultValue);
		value = structSeq.getValue();
		assertEquals(defaultValue.size(), value.size());
		
		
		//Test removing a struct by key-value pair
		structSeq.removeStructWithPropertyThatMatches("actor_name", "Keannu Reaves");
	
		//Should now only be 2
		value = structSeq.getValue();
		assertEquals(2, value.size());
		
		//Should be empty since actor is no longer in data set 
		Map<String, Object> map = structSeq.getStructByPropertyAndValue("actor_name", "Keannu Reaves");
		assertNull(map);
	}
}

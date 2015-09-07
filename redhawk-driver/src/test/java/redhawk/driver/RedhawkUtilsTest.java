package redhawk.driver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.omg.CORBA.Any;

import BULKIO.StreamSRI;
import CF.DataType;
import CF.DataTypeHelper;
import CF.PropertiesHelper;
import redhawk.RedhawkTestBase;
import redhawk.driver.eventchannel.listeners.PropertyChange;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;

@Ignore("Will not work until REDHAWKTestBase is setup proper. That either means giving it an actual RHDriver object connected to a domain "
		+ "or mocking everything the right way...")
public class RedhawkUtilsTest extends RedhawkTestBase {

	private final String DATA_FOLDER = "src/test/resources/data"; 

	private StreamSRI sri1;
	private StreamSRI sri2;
	
	@Before
	public void setup() throws MultipleResourceException, ResourceNotFoundException, ConnectionException, IOException, CORBAException {
		super.setup();
		sri1 = new StreamSRI();
		sri1.streamID = "sri";
		
		DataType t = new DataType();
		t.id = "test";
		t.value = RedhawkUtils.createAny(redhawk.getOrb(), "value");
		
		sri1.keywords = new DataType[]{t};
		sri1.blocking = true;
		sri1.hversion = 1;
		sri1.xstart = 2.0;
		sri1.xdelta = 1.0;
		sri1.xunits = 2;
		sri1.subsize = 2;
		sri1.ystart = 5;
		sri1.ydelta = 8;
		sri1.yunits = 9;
		sri1.mode = 8;
		
		sri2 = new StreamSRI();
		sri2.streamID = "sri";
		sri2.keywords = new DataType[]{t};
		sri2.blocking = true;
		sri2.hversion = 1;
		sri2.xstart = 2.0;
		sri2.xdelta = 1.0;
		sri2.xunits = 2;
		sri2.subsize = 2;
		sri2.ystart = 5;
		sri2.ydelta = 8;
		sri2.yunits = 9;
		sri2.mode = 8;
	}
	
	
	@Test
	public void testUnmarshalSadFile() throws IOException {
		InputStream data = new FileInputStream(DATA_FOLDER+"/"+"myWaveform.sad.xml");
		Softwareassembly sadObject = RedhawkUtils.unMarshalSadFile(data);
		Assert.assertEquals("Hello World WaveForm.", sadObject.getDescription().trim());
	}
	
	@Test(expected=IOException.class)
	public void testUnmarshalSadFileWithException() throws IOException {
		RedhawkUtils.unMarshalSadFile(null);
	}
	
	@Test
	public void testCreateAny() {
		
		Map<String, Object> props = new HashMap<>();
		props.put("something", "test");
		props.put("two", new Short("1"));
		props.put("boolean", new Boolean("True"));
		props.put("long", new Long("5"));
		props.put("char", new Character('h'));
		props.put("double", new Double("6.0"));
		props.put("float", new Float("6.0"));
		props.put("integer", new Integer("6"));
		props.put("byte", new Byte("6"));

		Map<String, Object> map = new HashMap<>();
		map.put("test", "value");
		props.put("map", map);
		
		for(Object obj : props.values()){
			Any any = RedhawkUtils.createAny(redhawk.getOrb(), obj);
			Assert.assertTrue(any != null);
		}
	}
	
	
	@Test
	public void testCompareStreamSRI() {
		Assert.assertTrue(RedhawkUtils.compareStreamSRI(sri1, sri2));
		
		sri1.streamID = "HI";
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri1.streamID = "sri";

		sri2.streamID = "HI";
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri2.streamID = "sri";		
		
		sri1.hversion = 0;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri1.hversion = 1;

		sri2.hversion = 0;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri2.hversion = 1;		
		
		sri1.xstart = 0.0;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri1.xstart = 2.0;

		sri2.xstart = 0.0;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri2.xstart = 2.0;	
		
		sri1.xdelta = 0.0;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri1.xdelta = 1.0;

		sri2.xdelta = 0.0;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri2.xdelta = 1.0;
		
		sri1.xunits = 3;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri1.xunits = 9;
		
		sri2.xunits = 3;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri2.xunits = 9;		

		sri1.blocking = false;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri1.blocking = true;
		
		sri2.blocking = false;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri2.blocking = true;
		
		sri1.subsize = 3;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri1.subsize = 2;
		
		sri2.subsize = 3;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri2.subsize = 2;
		
		sri1.ystart = 3;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri1.ystart = 5;
		
		sri2.ystart = 3;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri2.ystart = 5;
		
		sri1.ydelta = 3;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri1.ydelta = 8;
		
		sri2.ydelta = 3;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri2.ydelta = 8;
		
		sri1.mode = 3;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri1.mode = 8;
		
		sri2.mode = 3;
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri2.mode = 8;
		
		DataType[] old = sri1.keywords;
		
		DataType t = new DataType();
		t.id = "different";
		t.value = RedhawkUtils.createAny(redhawk.getOrb(), "diff");
		
		sri1.keywords = new DataType[]{t};
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri1.keywords = old;

		sri2.keywords = new DataType[]{t};
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri2.keywords = old;

		sri1.keywords = new DataType[0];
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri1.keywords = old;

		sri2.keywords = new DataType[0];
		Assert.assertFalse(RedhawkUtils.compareStreamSRI(sri1, sri2));
		sri2.keywords = old;
		
	}
	
	@Test
	public void testConvertPropertiesAnyToMap() {
		DataType t = new DataType();
		t.id = "different";
		t.value = RedhawkUtils.createAny(redhawk.getOrb(), "diff");
		
		DataType[] s = new DataType[]{t};
		
		Any any = redhawk.getOrb().create_any();
		PropertiesHelper.insert(any, s);
		
		DataType q = new DataType();
		q.id = "tt";
		q.value = any;		
		
		DataType[] r = new DataType[]{q};
		
		Any any2 = redhawk.getOrb().create_any();
		PropertiesHelper.insert(any2, r);
		Map<String, Object> propertiesMap = RedhawkUtils.convertPropertiesAnyToMap(any2);
		
		Assert.assertTrue(propertiesMap.size() == 1);
		
	}

	@Test
	public void testConvertAny() {
		PropertyChange change = new  PropertyChange();
		change.setSourceId("sourceId");
		change.setSourceName("sourceName");
		Map<String, Object> properties = new HashMap<>();
		properties.put("test", "string");
		

		
		DataType t = new DataType();
		t.id = "different";
		t.value = RedhawkUtils.createAny(redhawk.getOrb(), "diff");
		
		DataType[] s = new DataType[]{t};
		properties.put("r", s);
		
		Any any = redhawk.getOrb().create_any();
		PropertiesHelper.insert(any, s);
		
		DataType q = new DataType();
		q.id = "tt";
		q.value = any;		
		
		DataType[] r = new DataType[]{q};
		
		Any any2 = redhawk.getOrb().create_any();
		PropertiesHelper.insert(any2, r);		
		Any[] arr = new Any[]{any2};
		properties.put("q", arr);
		
		change.setProperties(properties);
		Map<String, Object> propertiesMap = RedhawkUtils.convertAny(change);
		Assert.assertTrue(propertiesMap.size() == 3);
		
		
	}
	
//	public static Map<String, Object> convertAny(PropertyChange message){

	
}

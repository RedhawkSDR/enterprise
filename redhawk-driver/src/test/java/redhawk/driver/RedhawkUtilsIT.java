/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package redhawk.driver;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.omg.CORBA.Any;

import BULKIO.StreamSRI;
import CF.DataType;
import CF.PropertiesHelper;
import redhawk.driver.eventchannel.listeners.PropertyChange;

public class RedhawkUtilsIT {
	private static StreamSRI sri1;
	
	private static StreamSRI sri2;
	
	private static Redhawk redhawk; 

	@BeforeClass
	public static void setup() {
		redhawk = new RedhawkDriver(); 
		
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
	public void testCreateAnyWithMap(){
		Map<String, Object> props = new HashMap<>();
		props.put("String", "Hola");
		props.put("Double", 10.0);
		
		RedhawkUtils.createAny(redhawk.getOrb(), props);
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
}

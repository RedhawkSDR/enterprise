package redhawk.websocket.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.ossie.properties.AnyUtils;

import CF.DataType;
import StandardEvent.SourceCategoryType;
import redhawk.driver.eventchannel.listeners.PropertyChange;
import redhawk.websocket.model.PropertyChangeModel;
import redhawk.websocket.model.SourceCategory;
import redhawk.websocket.utils.EventChannelConverter;

public class EventChannelConverterTest {
	PropertyChange change; 
	@Before
	public void setup(){
		HashMap<String, Object> props = new HashMap<>(); 
		props.put("hello", "world");
		change = new PropertyChange(); 
		change.setSourceId("sourceId");
		change.setSourceName("sourceName");
		
		//TODO: Can't use mockito on any of the final classes like DataType, DomainManagementObjectAddedEventType it's a final class 
		DataType[] dataType = new DataType[1];
		dataType[0] = new DataType("id", AnyUtils.stringToAny("hello world", "string"));
		
		//Any[] anyArray = new Any[1];
		//anyArray[0] = AnyUtils.toAny(dataType, TypeCode.create_tc(dataType.getClass()));
		
		//props.put("any", anyArray);
		props.put("dt", dataType);
		change.setProperties(props);

	}
	
	
	@Test
	public void test(){
		PropertyChangeModel model = EventChannelConverter.convertToPropertyChangeModel(change);
		
		assertEquals("sourceId", model.getSourceId());
		assertEquals("sourceName", model.getSourceName());
		assertEquals(2, model.getProperties().size());
	}
	
	@Test
	public void testSourceCategoryConversion(){
		assertEquals(SourceCategory.APPLICATION, EventChannelConverter.getSourceCategory(SourceCategoryType.APPLICATION.value()));
		assertEquals(SourceCategory.APPLICATION_FACTORY, EventChannelConverter.getSourceCategory(SourceCategoryType.APPLICATION_FACTORY.value()));
		assertEquals(SourceCategory.DEVICE, EventChannelConverter.getSourceCategory(SourceCategoryType.DEVICE.value()));
		assertEquals(SourceCategory.DEVICE_MANAGER, EventChannelConverter.getSourceCategory(SourceCategoryType.DEVICE_MANAGER.value()));
		assertEquals(SourceCategory.SERVICE, EventChannelConverter.getSourceCategory(SourceCategoryType.SERVICE.value()));
		assertEquals(SourceCategory.UNKNOWN, EventChannelConverter.getSourceCategory(10001));
	}
	
	@Test
	public void testFailOnDomainManagementObjectToModel(){
		try{
			EventChannelConverter.convertDomainManagementObjectToModel("");
			fail("Should have thrown an exception");
		}catch(Exception ex){
			assertEquals(ClassCastException.class, ex.getClass());
		}
	}
}

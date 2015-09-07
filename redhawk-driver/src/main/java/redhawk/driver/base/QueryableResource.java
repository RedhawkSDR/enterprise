package redhawk.driver.base;

import java.util.Map;

import redhawk.driver.properties.RedhawkProperty;

public interface QueryableResource {

    public Map<String, RedhawkProperty> getProperties();
    public <T> T getProperty(String ... propertyNames);
    
}

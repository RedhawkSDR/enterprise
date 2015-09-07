package redhawk.driver.component;

import CF.ComponentType;
import redhawk.driver.base.PortBackedObject;
import redhawk.driver.exceptions.ComponentStartException;
import redhawk.driver.exceptions.ComponentStopException;

public interface RedhawkComponent extends PortBackedObject {
    String getName();
    ComponentType getCorbaObj();
    void start() throws ComponentStartException;
    boolean started();
    void stop() throws ComponentStopException;
}

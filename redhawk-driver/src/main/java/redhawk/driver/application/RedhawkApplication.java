package redhawk.driver.application;

import java.io.IOException;
import java.util.List;

import CF.Application;
import redhawk.driver.base.QueryableResource;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.ApplicationStartException;
import redhawk.driver.exceptions.ApplicationStopException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;

public interface RedhawkApplication extends QueryableResource {
    List<RedhawkComponent> getComponents();
    List<RedhawkComponent> getComponentsByName(String name);
    RedhawkComponent getComponentByName(String name) throws MultipleResourceException, ResourceNotFoundException;
    String getName();
    String getIdentifier();
    RedhawkDomainManager getRedhawkDomainManager();
    Application getCorbaObj();
	Softwareassembly getAssembly() throws IOException;
    void start() throws ApplicationStartException;
    void stop() throws ApplicationStopException;
    void release() throws ApplicationReleaseException;
    RedhawkPort getExternalPort(String name) throws ResourceNotFoundException, IOException;
    List<RedhawkPort> getExternalPorts() throws IOException;
    boolean isStarted();
}
package redhawk.driver.port;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import redhawk.driver.bulkio.Packet;


public abstract class PortListener<TParsedClass> {

	private static Logger logger = Logger.getLogger(PortListener.class.getName());
	private int maxQueueSize = 8000;
	
	public Class getPortType(){
		Type t = getClass().getGenericSuperclass();
		logger.info(t.getClass().getName());
		ParameterizedType p = (ParameterizedType) t;
		Class<?> serviceImplClass = (Class<?>) p.getActualTypeArguments()[0];
		return serviceImplClass;
	}
	
	public abstract void onReceive(Packet<TParsedClass> packet);

	public int getMaxQueueSize() {
		return maxQueueSize;
	}
	
	public void setMaxQueueSize(int maxQueueSize){
		this.maxQueueSize = maxQueueSize;
	}
	
}
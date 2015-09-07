package redhawk.driver.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class JAXBContextCacheUtil {
	private static Logger logger = Logger.getLogger(JAXBContextCacheUtil.class.getName());
	
	private static JAXBContextCacheUtil instance = null; 
	
	private Map<String, JAXBContext> jaxbContextCache = new HashMap<>(); 
	
	protected JAXBContextCacheUtil(){
		
	}
	
	public static JAXBContextCacheUtil getInstance(){
		if(instance==null){
			instance = new JAXBContextCacheUtil(); 
		}
		
		return instance;
	}
	
	public JAXBContext getContext(String schemaContextPath) throws JAXBException{
		if(!jaxbContextCache.containsKey(schemaContextPath)){
			logger.fine("Creating new context");
			JAXBContext context = JAXBContext.newInstance(schemaContextPath, JAXBContextCacheUtil.class.getClassLoader());
			jaxbContextCache.put(schemaContextPath, context); 
			return context; 
		}else{
			logger.fine("Using context in cache"); 
			return jaxbContextCache.get(schemaContextPath);
		}
	}
	
	
}
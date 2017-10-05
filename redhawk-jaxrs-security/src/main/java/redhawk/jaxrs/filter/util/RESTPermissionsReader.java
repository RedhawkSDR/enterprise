package redhawk.jaxrs.filter.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RESTPermissionsReader {
	private Logger logger = LoggerFactory.getLogger(RESTPermissionsReader.class.getName());
	
	private Map<String, RestMethodAuthorizationMapper> pathToAuth = new HashMap<>();
	
	private final String header = "URL,Method,Role";
	
	public Map<String, RestMethodAuthorizationMapper> readRestFile(String file) throws IOException {
		try(Stream<String>  stream = Files.lines(Paths.get(file))){
			logger.debug("Looping over file");
			stream.forEach(this::process);
			
			return pathToAuth;
		} catch (RuntimeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IOException("Unable to read permission file: "+file, e);
		}
	}
	
	private void process(String s) {
		if(!s.equals(header)) {
			String[] entries = s.split(",");
			String path = null;
			String[] methods = null;
			for(int i=0; i<entries.length; i++) {
				//Path
				if(i==0) {
					path = entries[i];
					logger.debug("Path : "+path);
					//TODO: Possibly consolidate update logic in a method 
					RestMethodAuthorizationMapper restMapper = pathToAuth.get(path);
					//If path not already there update map
					if(restMapper==null) {
						restMapper = new RestMethodAuthorizationMapper();
						restMapper.setPath(path);
						pathToAuth.put(path, restMapper);
					}
				}else if(i==1) {
					//TODO: I think standard is to put quotes around multiple entries
					if(!entries[i].equals("")) {
						methods = entries[i].trim().split(";");
					}else {
						methods = new String[] {"ALL"};
					}
					
					//At this point there should be a path available
					RestMethodAuthorizationMapper restMapper = pathToAuth.get(path);
					
					if(restMapper==null)
						throw new RuntimeException("Path should be present at this point in the algorithm");
					
					//Add methods
					for(String method : methods) {
						logger.debug("Adding method: "+method);
						restMapper.addMethodToRole(method, new HashSet<String>());
					}
				}else if(i==2) {
					//Add roles to methods
					//This will be initialized by time it gets here
					//TODO: I think standard is to put quotes around multiple entries
					Set<String> roles = Arrays.stream(entries[i].split(";")).collect(Collectors.toSet());
					
					//At this point there should be a path available
					RestMethodAuthorizationMapper restMapper = pathToAuth.get(path);
					
					if(restMapper==null)
						throw new RuntimeException("Path should be present at this point in the algorithm");
					
					
					for(String method : methods) {
						logger.debug("Adding roles: "+roles+" to method "+method);
						restMapper.addMethodToRole(method, roles);
					}
				}
			}
		}
	}
}

package redhawk.jaxrs.filter.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

public class RESTPermissionsReader {
	private Logger logger = LoggerFactory.getLogger(RESTPermissionsReader.class.getName());

	public Map<String, RestMethodAuthorizationMapper> readRestFile(String file, Boolean strictPermissions) throws IOException {
		try {
			CsvParserSettings settings = new CsvParserSettings();
			settings.getFormat().setLineSeparator("\n");
			CsvParser parser = new CsvParser(settings);
			
			List<String[]> allRows = parser.parseAll(new FileReader(file));

			return this.process(allRows, strictPermissions);
		} catch (RuntimeException | IOException e) {
			logger.error("Error reading REST permission file.", e);
			throw new IOException("Unable to read permission file: " + file, e);
		}
	}

	private Map<String, RestMethodAuthorizationMapper> process(List<String[]> allRows, Boolean strictPerms) {
		Map<String, RestMethodAuthorizationMapper> pathToAuth = new HashMap<>();
		String[] methods = new String[] {}; 
		String path = null;
		/*
		 * Loop through properties to return the Path to Authorization map 
		 */
		for (String[] rows : allRows) {
			RestMethodAuthorizationMapper restMapper = null;
			for (int i = 0; i < rows.length; i++) {
				if(i == 0) {
					path = rows[i];
					logger.debug("Path: "+path);
					
					restMapper = pathToAuth.get(path);
					// If path not already there update map
					if (restMapper == null) {
						restMapper = new RestMethodAuthorizationMapper(path, strictPerms);
						pathToAuth.put(path, restMapper);
					}
				}else if (i == 1) {
					// TODO: I think standard is to put quotes around multiple entries
					if (rows[i]!=null && !rows[i].equals("")) {
						methods = rows[i].trim().split(",");
					} else {
						methods = new String[] { "ALL" };
					}

					// At this point there should be a path available
					restMapper = pathToAuth.get(path);

					if (restMapper == null)
						throw new RuntimeException("Path should be present at this point in the algorithm");

					// Add methods
					for (String method : methods) {
						logger.debug("Adding method: " + method);
						restMapper.addMethodToRole(method, new HashSet<String>());
					}
				} else if (i == 2) {
					// Add roles to methods
					// This will be initialized by time it gets here
					// TODO: I think standard is to put quotes around multiple entries
					Set<String> roles = Arrays.stream(rows[i].split(",")).collect(Collectors.toSet());

					// At this point there should be a path available
					restMapper = pathToAuth.get(path);

					if (restMapper == null)
						throw new RuntimeException("Path should be present at this point in the algorithm");

					for (String method : methods) {
						logger.debug("Adding roles: " + roles + " to method " + method);
						restMapper.addMethodToRole(method, roles);
					}
				}
			}
		}
		
		return pathToAuth;
	}
}

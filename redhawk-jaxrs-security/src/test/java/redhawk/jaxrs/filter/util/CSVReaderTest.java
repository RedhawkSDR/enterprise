package redhawk.jaxrs.filter.util;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

//RFC 4180
//CSV Parser Options: https://github.com/uniVocity/csv-parsers-comparison
public class CSVReaderTest {
	Logger logger = LoggerFactory.getLogger(CSVReaderTest.class);
	
	@Test
	public void uniVocityCSVReader() throws FileNotFoundException {
		CsvParserSettings settings = new CsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		CsvParser parser = new CsvParser(settings);
		List<String[]> allRows = parser.parseAll(new FileReader("src/test/resources/admin-only.csv"));
		
		for(String[] rows : allRows) {
			for(int i=0; i<rows.length; i++) {
				logger.info("Row "+i+":"+rows[i]);				
			}
		}
	}
	
	@Test
	public void testURI() throws URISyntaxException {
		//Jetty home ends up looking like a uri 
		String test1 = "file:/hello/world";
		
		URI uri = new URI(test1);
		assertEquals("/hello/world", uri.getPath());
	
		//Make sure uri works with plain string
		test1 = "/hello/world";
		uri = new URI(test1);
		assertEquals("/hello/world", uri.getPath());
	}
}

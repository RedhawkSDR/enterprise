package redhawk.jaxrs.filter.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
}

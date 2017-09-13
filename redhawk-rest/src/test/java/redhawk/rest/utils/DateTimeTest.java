package redhawk.rest.utils;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

public class DateTimeTest {
	@Test
	public void dateTimeManipulation() {
		LocalDateTime timePoint = LocalDateTime.now();
		
		String DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss";
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_FORMAT);
        assertNotNull(timePoint.format(format));
	}
}

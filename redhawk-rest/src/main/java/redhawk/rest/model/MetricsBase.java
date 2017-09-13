package redhawk.rest.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MetricsBase {
	private String dateTime; 
	
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss";

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_FORMAT);

	
	public String getDateTime() {
		LocalDateTime timePoint = LocalDateTime.now();

		return timePoint.format(format);
	}
}

package redhawk;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import redhawk.driver.RedhawkDriver;

/**
 * Default location of properties file is classpath:/redhawk-driver.properties, can be overridden with
 * -Dredhawk.driver.test.properties=file:/path/to/redhawk-driver.properties
 *
 */
public class RedhawkBaseTest {

    static RedhawkDriver driver;
    public static String DEFAULT_REDHAWK_PROPERTIES_FILE = "classpath:/redhawk-driver.properties";
    public static String OVERRIDE_REDHAWK_PROPERTIES = "redhawk.driver.test.properties";

	@AfterClass
	public static void after() {
		driver.disconnect();
	}

	@BeforeClass
	public static void before() throws IOException {
		String propFile = System.getProperty(OVERRIDE_REDHAWK_PROPERTIES, DEFAULT_REDHAWK_PROPERTIES_FILE);
		URL url = propFile.startsWith("classpath:") ? RedhawkMiscTests.class.getResource(propFile.substring(10)) : new URL(propFile);
		Properties props = new Properties();
		props.load(url.openStream());
		driver = new RedhawkDriver(props.getProperty("hostname"), Integer.parseInt(props.getProperty("port", "2809")));
		System.out.println("Driver: " + driver);
	}
}

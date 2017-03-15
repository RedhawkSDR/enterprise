package $package;

import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;

/**
 * Hello redhawk!
 *
 */
public class App 
{
	private static String nameServerHost = "127.0.0.1";
	
	private static Integer nameServerPort = 2809;
	
    public static void main( String[] args ) throws Exception
    {
    	Redhawk driver = new RedhawkDriver(nameServerHost, nameServerPort);
    	
        System.out.println( "Hello Redhawk Domain "+driver.getDomain("REDHAWK_DEV"));
    }
}

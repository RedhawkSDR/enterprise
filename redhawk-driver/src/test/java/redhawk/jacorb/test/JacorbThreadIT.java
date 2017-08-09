package redhawk.jacorb.test;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Properties;

import org.junit.Test;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;

public class JacorbThreadIT {
	@Test
	public void test() throws MultipleResourceException, CORBAException, InterruptedException {
		Integer threadCount = ManagementFactory.getThreadMXBean().getThreadCount();
		
		
		System.out.println("===============Prior to RH=====================");
		System.out.println("Thread count "+threadCount);
		System.out.println(this.crunchifyGenerateThreadDump());
		
		Properties props = new Properties(); 
		props.put("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
		props.put("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");
		props.put("jacorb.log.default.verbosity", 0);
		//See page 188 of jacorb docs http://www.jacorb.org/releases/3.8/ProgrammingGuide.pdf
		props.put("jacorb.connection.client.max_idle_receptor_threads", 2);
		//props.put("jacorb.connection.client.idle_timeout", 500);
		//props.put("jacorb.connection.client.pending_reply_timeout", 500);
		//props.put("jacorb.connection.client.timeout_ignores_pending_messages", "true");
		//props.put("jacorb.connection.client.max_receptor_threads", 2);
		//props.put("jacorb.connection.server.timeout", 1000); //Probably only need server side one
		//props.put("jacorb.connection.nonblocking", "true");

		RedhawkDriver driver = new RedhawkDriver("localhost", 2809, props); 
		if(!driver.getDomain().getApplications().isEmpty()) {
			RedhawkApplication app = driver.getDomain().getApplications().get(0);
			
			for(RedhawkComponent comp : app.getComponents()) {
				comp.started();
			}			
		}
		
		System.out.println("===============Post RH Creation=====================");
		Thread.sleep(5000l);
		threadCount = ManagementFactory.getThreadMXBean().getThreadCount();
		System.out.println("Thread count "+threadCount);
		System.out.println(this.crunchifyGenerateThreadDump());
		
		driver.disconnect();
		System.out.println("===============Post RH Disconnect=====================");
		Thread.sleep(5000l);
		threadCount = ManagementFactory.getThreadMXBean().getThreadCount();
		System.out.println("Thread count "+threadCount);
		System.out.println(this.crunchifyGenerateThreadDump());
	}
	
	@Test
	public void testSetJacorbProperties() throws MultipleResourceException, CORBAException {
		Properties props = new Properties(); 
		//Props to make sure jacorb is used
		props.put("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
		props.put("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");
		
		//Properties for jacorb
		props.put("jacorb.connection.client.max_idle_receptor_threads", 2);
		props.put("jacorb.connection.client.max_receptor_threads", 2);
		
		RedhawkDriver driver = new RedhawkDriver("localhost", 2809, props); 
		//RedhawkDriver driver = new RedhawkDriver("localhost", 2809); 
		driver.getDomain();
	}
	
	/*
	 * Code from crunchify 
	 * https://cdn.crunchify.com/wp-content/uploads/2013/07/Generate-Java-Thread-Dump-Programmatically.png
	 */
	public static String crunchifyGenerateThreadDump() {
		final StringBuilder dump = new StringBuilder(); 
		final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		final ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), 100);
		
		for(ThreadInfo threadInfo : threadInfos) {
			dump.append('"');
			dump.append(threadInfo.getThreadName());
			dump.append("\" ");
			final Thread.State state = threadInfo.getThreadState();
			
			dump.append("\n java.lang.Thread.State: ");
			dump.append(state);
			
			final StackTraceElement[] stackTraceElements = threadInfo.getStackTrace();
			
			for(final StackTraceElement stackTraceElement : stackTraceElements) {
				dump.append("\n \t at");
				dump.append(stackTraceElement);
			}
			
			dump.append("\n\n");
		}
		
		return dump.toString();
	}
	
}

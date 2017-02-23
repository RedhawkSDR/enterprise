package EventSpitter.java;

import java.util.Properties;
import java.util.UUID;

public class EventSpitter extends EventSpitter_base {

	public EventSpitter() {
		super();
	}

	public void constructor() {
	}

	/**
	 * This method runs continuously. Every second, it will send a properties
	 * struct to the messages port. In the waveform, this should be hooked up to
	 * an event channel.
	 */
	protected int serviceFunction() {
		properties_struct props = new properties_struct();
		props.foo.setValue(String.format("foo-%s", UUID.randomUUID()));
		props.bar.setValue(1.6f);

		this.port_messages.sendMessage(props);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		return NOOP;
	}

	/**
	 * Set additional options for ORB startup. For example:
	 *
	 * orbProps.put("com.sun.CORBA.giop.ORBFragmentSize",
	 * Integer.toString(fragSize));
	 *
	 * @param orbProps
	 */
	public static void configureOrb(final Properties orbProps) {
	}

}

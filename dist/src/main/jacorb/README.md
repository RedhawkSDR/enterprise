## REDHAWK Driver w/ JacORB ##
***

In order to get REDHAWK Driver to use JacORB instead of the Sun ORB in KARAF you need to do configure the jvm to use jacorb.

### Configuring KARAF with JacORB ###
***

Add these properties to your ${KARAF_HOME}/system.properties: 

	org.omg.CORBA.ORBSingletonClass=org.jacorb.orb.ORBSingleton
	org.omg.PortableInterceptor.ORBInitializerClass.standard_init=org.jacorb.orb.standardInterceptors.IORInterceptorInitializer
	org.omg.CORBA.ORBClass=org.jacorb.orb.ORB
	
Once that's in place you also need to add the jacorb dependencies to KARAF by placing them in the $KARAF_HOME/ext directory.

	cp jacorb-deps/* $karaf.home/lib/ext 
	
### Configuring JREPL with JacORB ###
***

Add these parameters to your EXTRA_JAVA_OPTS: 

	-Xbootclasspath/p:../jacorb-deps

Once you login you'll also need to set these two system properties: 

	System.setProperty("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
	System.setProperty("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");
	

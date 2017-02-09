## Notes on IDL ##
***

Need bulkiointerfaces, cfinterfaces, ossie, frontendinterfaces minimum as dependencies either in NEXUS or as a submodule in here. 
How to best accomplish this?

Redhawk Driver has maven dependencies on the following:

* bulkiointerfaces
* cfinterfaces
* ossie
* frontendinterfaces

One location you can find these jars are here: 

	/usr/local/redhawk/core/lib/log4j-1.2.15.jar
	/usr/local/redhawk/core/lib/burstio.jar
	/usr/local/redhawk/core/lib/apache-commons-lang-2.4.jar
	/usr/local/redhawk/core/lib/BURSTIOInterfaces.jar
	/usr/local/redhawk/core/lib/BULKIOInterfaces.src.jar
	/usr/local/redhawk/core/lib/bulkio.src.jar
	/usr/local/redhawk/core/lib/frontend.jar
	/usr/local/redhawk/core/lib/ossie.jar
	/usr/local/redhawk/core/lib/BULKIOInterfaces.jar
	/usr/local/redhawk/core/lib/CFInterfaces.jar
	/usr/local/redhawk/core/lib/bulkio.jar
	/usr/local/redhawk/core/lib/FRONTENDInterfaces.jar

The IDL's for generating the 4 jars that Redhawk Integration currently uses are here:

* BULKIO: https://github.com/RedhawkSDR/core-framework/tree/develop-2.0/bulkioInterfaces/idl/ossie/BULKIO
* FRONTENDINTERFACES: https://github.com/RedhawkSDR/core-framework/tree/develop-2.0/frontendInterfaces/idl/redhawk/FRONTEND
* cfinterfaces: https://github.com/RedhawkSDR/core-framework/tree/develop-2.0/redhawk/src/idl/ossie/CF
* ossie: 

Certain files likely need to be removed before this is released: 

./redhawk-driver/src/main/java/redhawk/driver/devicemanager/impl/IOCase.java
./redhawk-driver/src/main/java/redhawk/driver/xml/IndentingXMLStreamWriter.java 

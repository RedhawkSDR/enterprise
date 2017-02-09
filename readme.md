# Redhawk Integration #
***

* [Redhawk Driver](redhawk-driver/readme.md): Library that enables Java developer to interact with REDHAWK. Provides the ability to command & control your REDHAWK instance programatically.  
* [Redhawk Connector](redhawk-connector): Uses OSGi's Managed Service Factory interface to allow users to register pre-configured instances of REDHAWK connections into a KARAF container. 
* [Redhawk REST](redhawk-rest): Provides REST Service to command and control REDHAWK Instance
* [Redhawk Websocket](redhawk-websocket): Enables users to connect to a port of event channel and provide data back to a GUI using a websocket. 
* [Camel REDHAWK](camel-redhawk): Provides REDHAWK Camel Component. S 
* [Redhawk Feature](redhawk-featutre): Contains Feature for installing all of the above REDHAWK assets into base KARAF 
* [Redhawk Karaf Assembly](redhawk-karaf-assembly): Builds a custom KARAF distribution with a Redhawk Driver, Redhawk Connector, Redhawk Websocket, Redhawk REST and Camel REDHAWK pre-installed.  

## Building From Source ## 
***

The REHDAWK Integration assets depend on some dependencies that Core Framework builds:

* ossie
* BULKIOInterfaces
* FRONTENDInterfaces
* CFInterfaces

In order to make sure those dependencies are avaialbe for you when building from source. You must first run the profile:

	mvn -PCFDependencies clean install 
	
Running that command will put the Coreframework dependencies in your defined m2 repository so the default build of all the artifacts will have all the dependencies it needs. 

## Running Integration Tests ##
***

Currently the integration tests are run off a local install of REDHAWK. The domain is run locally and uses the following defaults:

* port: 2809
* domainName: REDHAWK_DEV

Once you have REDHAWK installed and a domain configured with the defaults listed above. You can run this command to get the integration tests to work:

	mvn -PlocalIT clean install 

This will be made more configurable in the future and will eventually also have an option to run off a docker image of REDHAWK. 


	


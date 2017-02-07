# REDHAWK Driver #
***

# Base Karaf Usage #
***

### Redhawk Driver ### 
***

To install into karaf base run the following commands

	feature:repo-add mvn:redhawk/redhawk-feature/${project.version}/xml/features
	feature:install redbus-redhawk-driver

#### Sample Usage with Groovy Shell Script ####

Install groovy shell feature into karaf:

	feature:repo-add mvn:redbus.services.shell/groovy/1.0.3-SNAPSHOT/xml/features
	feature:install 
	
Then you can run the following commands to test the driver: 

	redbus console
	groovy #puts you in command console 
	import redhawk.driver.* #Imports Redhawk Driver dependencies into console 
	rh = new RedhawkDriver("localhost", 2809) #Creates a new driver 
	rh.domains #Lists your domains...	
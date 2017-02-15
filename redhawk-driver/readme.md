# REDHAWK Driver #
***

## Sample Usage with Java Shell ##
***


## Base Karaf Usage ##
***

	feature:repo-add mvn:redhawk/redhawk-feature/${project.version}/xml/features
	feature:install redbus-redhawk-driver


	
Then you can run the following commands to test the driver: 

	redbus console
	groovy #puts you in command console 
	import redhawk.driver.* #Imports Redhawk Driver dependencies into console 
	rh = new RedhawkDriver("localhost", 2809) #Creates a new driver 
	rh.domains #Lists your domains...	
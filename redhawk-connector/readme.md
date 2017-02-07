# REDHAWK Connector #
***

### Redhawk Connector ###
***

To install into karaf base run the following commands  

	feature:repo-add mvn:redhawk/redhawk-feature/${project.version}/xml/features
	feature:install redbus-redhawk-connector
	
This feature installs the karaf webconsole as well so you can access the configuration for the connector via the web. To access the connector through the web hit this endpoint http://localhost:8181/system/console/configMgr . You'll be able to see the ManagedServiceFactory you just deployed for Redhawk. 
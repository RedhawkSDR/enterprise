### Redhawk Rest ###
***

To install into karaf base run the following commands:

	feature:repo-add mvn:redhawk/redhawk-feature/2.0.1.U2-SNAPSHOT/xml/features
	feature:install redbus-redhawk-rest
	
This feature installs cxf so that you can access REDHAWK via the web. The root endpoint for cxf is: http://localhost:8181/cxf/ . The root endpoint for REDHAWK rest is http://localhost:8181/cxf/redhawk . You'll need a REDHAWK Domain running to actually use the REST Endpoints. 

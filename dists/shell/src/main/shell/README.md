## REDHAWK Driver Shell Utility ##
***

Description: Simple shell utility that gives users the ability to interact with the REDHAWK Driver from a shell using a Java REPL.

### Instructions ###
***

* Run the jrelp script 
* Run <code>:eval driver-imports</code>
* At this point the shell is set up for you to run any REDHAWK Driver commands you like. 

### Example Commands ###
***

Start an Application: 

	driver = new RedhawkDriver(); 
	application = driver.getDomain("REDHAWK_DEV").createApplication("MyFirstApplication", "/waveforms/rh/FM_mono_demo/FM_mono_demo.sad.xml")
	
Start an Application: 

	application.start();

Release an Application:
	
	application.release();

### Trouble Shooting ### 
***

* If at initial launch of the script you run into issues finding a libansi.so file. It's because java cannot create a file in the default temporary directory. In order to fix this add the following to the jrepl script: 

	java -Djava.io.tmpdir=<path for temp dir> ...
	
  
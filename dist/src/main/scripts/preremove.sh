#!/bin/sh -e
## RPM %preun
## rpm-maven-plugin preremoveScriptlet

if [ "$1" = "0" ]; then
  # This is complete uninstall
  :
elif [ "$1" = "1" ]; then
  # This is an upgrade
  :
fi

if [[ -z "${RPM_INSTALL_PREFIX}" ]]
then
    HOME=${redhawk.user.home}
else
    HOME=${RPM_INSTALL_PREFIX}
fi

echo "Stopping KARAF if it's still running" 
cd ${HOME}
./${runtime.dir}/karaf-${karaf.version}/bin/stop

#Need to either ensure last command will have 0 status code(purpose of echo) or 
#Add in logic to determine if it's necessary to stop karaf. May need to submit a 
#bug because exit code of shell script should still be 0 if it failed to stop a 
#process because it isn't running. 
echo "Ready to uninstall ${project.artifactId}"
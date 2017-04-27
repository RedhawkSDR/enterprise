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

rm -rf ${runtime.basedir}
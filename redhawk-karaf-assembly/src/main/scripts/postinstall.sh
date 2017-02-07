#!/bin/sh -e
## RPM %post stage
## rpm-maven-plugin postinstallScriptlet

if [ "$1" = "1" ]; then
  # This is an initial installation
  :
elif [ "$1" = "2" ]; then
  # This is an upgrade
  :
fi

if [[ -z "${RPM_INSTALL_PREFIX}" ]]
then
    HOME=${redbus.user.home}
else
    HOME=${RPM_INSTALL_PREFIX}
fi

#Post Install Instructions 
%{__tar} -C ${HOME} -zxphf ${artifactId}-${version}.tar.gz --same-owner
%{__rm} -f ${artifactId}-${version}.tar.gz

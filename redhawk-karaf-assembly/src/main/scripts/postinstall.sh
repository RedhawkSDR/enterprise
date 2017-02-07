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
    HOME=${redhawk.user.home}
else
    HOME=${RPM_INSTALL_PREFIX}
fi

echo "Untaring REDHAWK Enterprise Karaf Distribution" 
cd ${HOME}
%{__tar} -zxphf ${artifactId}-${version}.tar.gz --same-owner
%{__mv} ${artifactId}-${version} ${runtime.dir}

#Update Permissions 
%{__chown} -R root:redhawk ${runtime.dir}
%{__chmod} -R 775 ${runtime.dir}

#Post Install Instructions 
%{__rm} -f ${artifactId}-${version}.tar.gz

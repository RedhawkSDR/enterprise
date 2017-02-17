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

echo "Untaring ${project.name}" 
cd ${HOME}
%{__tar} -zxphf ${runtime.dir}.tar.gz --same-owner

#Update Permissions 
%{__chown} -R root:redhawk ${runtime.dir}
%{__chmod} -R 775 ${runtime.dir}

#Post Install Instructions 
%{__rm} -f ${runtime.dir}.tar.gz

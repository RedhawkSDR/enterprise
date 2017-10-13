#!/bin/sh
# Create redhawk system account if it doesn't already exist
groupadd -r -f redhawk
if ! id redhawk &> /dev/null; then
  # -M is don't create home dir, -r is system account, -s is shell
  # -c is comment, -n is don't create group, -g is group name/id
  /usr/sbin/useradd -M -r -s /sbin/nologin \
    -c "REDHAWK System Account" -n -g redhawk redhawk > /dev/null
fi
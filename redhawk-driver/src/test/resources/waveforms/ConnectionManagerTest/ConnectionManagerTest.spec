
# RPM package for ConnectionManagerTest

# By default, the RPM will install to the standard REDHAWK SDR root location (/var/redhawk/sdr)
# You can override this at install time using --prefix /new/sdr/root when invoking rpm (preferred method, if you must)
%{!?_sdrroot: %global _sdrroot /var/redhawk/sdr}
%define _prefix %{_sdrroot}
Prefix: %{_prefix}

Name: ConnectionManagerTest
Summary: Waveform ConnectionManagerTest
Version: 1.0.0
Release: 1%{?dist}
License: None
Group: REDHAWK/Waveforms
Source: %{name}-%{version}.tar.gz
# Require the controller whose SPD is referenced
Requires: EventSpitter
# Require each referenced component
Requires: EventSpitter rh.SigGen
BuildArch: noarch
BuildRoot: %{_tmppath}/%{name}-%{version}

%description

%prep
%setup

%install
%__rm -rf $RPM_BUILD_ROOT
%__mkdir_p "$RPM_BUILD_ROOT%{_prefix}/dom/waveforms/ConnectionManagerTest"
%__install -m 644 ConnectionManagerTest.sad.xml $RPM_BUILD_ROOT%{_prefix}/dom/waveforms/ConnectionManagerTest/ConnectionManagerTest.sad.xml

%files
%defattr(-,redhawk,redhawk)
%dir %{_prefix}/dom/waveforms/ConnectionManagerTest
%{_prefix}/dom/waveforms/ConnectionManagerTest/ConnectionManagerTest.sad.xml

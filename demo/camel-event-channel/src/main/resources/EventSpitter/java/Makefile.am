ACLOCAL_AMFLAGS = -I m4 -I${OSSIEHOME}/share/aclocal/ossie
AUTOMAKE_OPTIONS = subdir-objects

EventSpitter_jar_CLASSPATH = $(SOFTPKG_CLASSPATH):$(REDHAWK_CLASSPATH):

EventSpitter.jar$(EXEEXT): $(EventSpitter_jar_SOURCES)
	mkdir -p bin
	$(JAVAC) -cp $(EventSpitter_jar_CLASSPATH) -g -d bin $(EventSpitter_jar_SOURCES)
	$(JAR) cf ./EventSpitter.jar -C bin .
	$(JAR) uf ./EventSpitter.jar -C src .

clean-local:
	rm -rf bin

distclean-local:
	rm -rf m4
	rm -f config.*
	rm -rf autom4te.cache
	rm -f acinclude.m4
	rm -f aclocal.m4
	rm -f configure
	rm -f depcomp
	rm -f install-sh
	rm -f ltmain.sh
	rm -f Makefile.in
	rm -f missing
	rm -rf .deps

ossieName = EventSpitter
bindir = $(prefix)/dom/components/EventSpitter/java/
bin_PROGRAMS = EventSpitter.jar
EventSpitter_jar_SOURCES := $(shell find ./src -name "*.java")

xmldir = $(prefix)/dom/components/EventSpitter/
dist_xml_DATA = ../EventSpitter.scd.xml ../EventSpitter.prf.xml ../EventSpitter.spd.xml

domdir = $(prefix)/dom/components/EventSpitter/java/
dist_dom_SCRIPTS = startJava.sh

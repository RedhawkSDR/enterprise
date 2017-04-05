ACLOCAL_AMFLAGS = -I m4 -I${OSSIEHOME}/share/aclocal/ossie
AUTOMAKE_OPTIONS = subdir-objects

MessageProducer_jar_CLASSPATH = $(SOFTPKG_CLASSPATH):$(REDHAWK_CLASSPATH):

MessageProducer.jar$(EXEEXT): $(MessageProducer_jar_SOURCES)
	mkdir -p bin
	$(JAVAC) -cp $(MessageProducer_jar_CLASSPATH) -g -d bin $(MessageProducer_jar_SOURCES)
	$(JAR) cf ./MessageProducer.jar -C bin .
	$(JAR) uf ./MessageProducer.jar -C src .

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

ossieName = MessageProducer
bindir = $(prefix)/dom/components/MessageProducer/java/
bin_PROGRAMS = MessageProducer.jar
MessageProducer_jar_SOURCES := $(shell find ./src -name "*.java")

xmldir = $(prefix)/dom/components/MessageProducer/
dist_xml_DATA = ../MessageProducer.scd.xml ../MessageProducer.prf.xml ../MessageProducer.spd.xml

domdir = $(prefix)/dom/components/MessageProducer/java/
dist_dom_SCRIPTS = startJava.sh

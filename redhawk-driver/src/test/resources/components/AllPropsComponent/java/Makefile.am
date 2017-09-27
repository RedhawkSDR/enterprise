ACLOCAL_AMFLAGS = -I m4 -I${OSSIEHOME}/share/aclocal/ossie
AUTOMAKE_OPTIONS = subdir-objects

AllPropsComponent_jar_CLASSPATH = $(SOFTPKG_CLASSPATH):$(REDHAWK_CLASSPATH):

AllPropsComponent.jar$(EXEEXT): $(AllPropsComponent_jar_SOURCES)
	mkdir -p bin
	$(JAVAC) -cp $(AllPropsComponent_jar_CLASSPATH) -g -d bin $(AllPropsComponent_jar_SOURCES)
	$(JAR) cf ./AllPropsComponent.jar -C bin .
	$(JAR) uf ./AllPropsComponent.jar -C src .

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

ossieName = AllPropsComponent
bindir = $(prefix)/dom/components/AllPropsComponent/java/
bin_PROGRAMS = AllPropsComponent.jar
AllPropsComponent_jar_SOURCES := $(shell find ./src -name "*.java")

xmldir = $(prefix)/dom/components/AllPropsComponent/
dist_xml_DATA = ../AllPropsComponent.scd.xml ../AllPropsComponent.prf.xml ../AllPropsComponent.spd.xml

domdir = $(prefix)/dom/components/AllPropsComponent/java/
dist_dom_SCRIPTS = startJava.sh

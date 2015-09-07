package redhawk.driver.xml;

import redhawk.driver.xml.model.sca.dcd.Deviceconfiguration;
import redhawk.driver.xml.model.sca.dmd.Domainmanagerconfiguration;
import redhawk.driver.xml.model.sca.dpd.Devicepkg;
import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.profile.Profile;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;
import redhawk.driver.xml.model.sca.scd.Softwarecomponent;
import redhawk.driver.xml.model.sca.spd.Softpkg;

public enum RedbusRedhawkScaXmlType {
	DEVICE_CONFIGURATION(Deviceconfiguration.class, "schemas/dcd.xsd", "dcd.xml", "urn:mil:jpeojtrs:sca:dcd"),
	DOMAIN_MANAGER_CONFIGURATION(Domainmanagerconfiguration.class, "schemas/dmd.xsd", "dmd.xml", "urn:mil:jpeojtrs:sca:dmd"),
	DEVICE_PACKAGE(Devicepkg.class, "schemas/dpd.xsd", "dpd.xml", "urn:mil:jpeojtrs:sca:dpd"),
	PROPERTIES(Properties.class, "schemas/prf.xsd", "prf.xml", "urn:mil:jpeojtrs:sca:prf"),
	PROFILE(Profile.class, "schemas/profile.xsd", "profile.xml", "urn:mil:jpeojtrs:sca:profile"),
	SOFTWARE_ASSEMBLY(Softwareassembly.class, "schemas/sad.xsd", "sad.xml", "urn:mil:jpeojtrs:sca:sad"),
	SOFTWARE_COMPONENT(Softwarecomponent.class, "schemas/scd.xsd", "scd.xml", "urn:mil:jpeojtrs:sca:scd"),
	SOFTWARE_PACKAGE(Softpkg.class, "schemas/spd.xsd", "spd.xml", "urn:mil:jpeojtrs:sca:spd");
	
	private final Class<? extends Object> schemaClass;
	private final String schemaFile;
	private final String xmlFileExtension;
	private final String xmlns;
	
	RedbusRedhawkScaXmlType(Class<? extends Object> schemaClass, String schemaFile, String xmlFileExtension, String xmlns) {
		this.schemaClass = schemaClass;
		this.schemaFile = schemaFile;
		this.xmlFileExtension = xmlFileExtension;
		this.xmlns = xmlns;
	}
	
	public static RedbusRedhawkScaXmlType getBySchemaClass(Class<? extends Object> schemaClass) {
		for (RedbusRedhawkScaXmlType xmlType : RedbusRedhawkScaXmlType.values()) {
			if (xmlType.schemaClass == schemaClass) {
				return xmlType;
			}
		}
		return null;
	}

	public String getSchemaFile() {
		return schemaFile;
	}

	public String getXmlFileExtension() {
		return xmlFileExtension;
	}

	public String getXmlns() {
		return xmlns;
	}
	
	public Class<? extends Object> getSchemaClass() {
		return schemaClass;
	}

}

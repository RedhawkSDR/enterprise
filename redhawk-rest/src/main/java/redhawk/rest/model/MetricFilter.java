package redhawk.rest.model;

public class MetricFilter {
	String[] components;
	
	String[] attributes;

	public MetricFilter() {
		this.components = new String[0];
		this.attributes = new String[0];
	}
	
	public MetricFilter(String[] components, String[] attributes) {
		this.components = components;
		this.attributes = attributes;
	}
	
	public String[] getComponents() {
		return components;
	}

	public void setComponents(String[] components) {
		this.components = components;
	}

	public String[] getAttributes() {
		return attributes;
	}

	public void setAttributes(String[] attributes) {
		this.attributes = attributes;
	}
}

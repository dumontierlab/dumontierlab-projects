package com.dumontierlab.ontocreator.ui.client.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ColumnMappingBean implements Serializable {

	public static enum ColumnMappingType {
		OWL_CLASS("OWL Class"), OWL_INDIVIDUAL("OWL Individual"), LITERAL("Literal");

		private final String label;

		ColumnMappingType(String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}
	};

	private String name;
	private int columnIndex;
	private ColumnMappingType mappingType;

	private Map<ColumnMappingBean, ColumnMappingRelationshipBean> relationships;

	// For literal mappings
	private String datatype;

	// For individual mappings
	private String instanceOf;

	// For class mappings
	private String subclassOf;
	private String equivalentTo;
	private String superclassOf;

	// For class and individual mappings
	private String uri;

	private ColumnMappingBean() {
		// for serialization only
	}

	public ColumnMappingBean(String name, int columnIndex, ColumnMappingType type) {
		relationships = new HashMap<ColumnMappingBean, ColumnMappingRelationshipBean>();
		this.name = name;
		this.columnIndex = columnIndex;
		mappingType = type;

	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public ColumnMappingType getMappingType() {
		return mappingType;
	}

	public void setMappingType(ColumnMappingType mappingType) {
		this.mappingType = mappingType;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getInstanceOf() {
		return instanceOf;
	}

	public void setInstanceOf(String instanceOf) {
		this.instanceOf = instanceOf;
	}

	public String getSubclassOf() {
		return subclassOf;
	}

	public void setSubclassOf(String subclassOf) {
		this.subclassOf = subclassOf;
	}

	public String getEquivalentTo() {
		return equivalentTo;
	}

	public void setEquivalentTo(String equivalentTo) {
		this.equivalentTo = equivalentTo;
	}

	public String getSuperclassOf() {
		return superclassOf;
	}

	public void setSuperclassOf(String superclassOf) {
		this.superclassOf = superclassOf;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public void addRelationship(ColumnMappingRelationshipBean relationship, ColumnMappingBean columnMapping) {
		relationships.put(columnMapping, relationship);
	}

	public Map<ColumnMappingBean, ColumnMappingRelationshipBean> getRelationships() {
		return relationships;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ColumnMappingBean)) {
			return false;
		}
		return name.equals(((ColumnMappingBean) obj).name);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = result + 31 + name.hashCode();
		return result;
	}

}

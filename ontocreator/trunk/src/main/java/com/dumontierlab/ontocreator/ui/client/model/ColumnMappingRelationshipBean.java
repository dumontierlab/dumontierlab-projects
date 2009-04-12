package com.dumontierlab.ontocreator.ui.client.model;

import java.io.Serializable;

public class ColumnMappingRelationshipBean implements Serializable {

	public static enum ColumnMappingRelationshipType {
		SUBCLASS_OF("Subclass of"), EQUIVALENT_TO("Equivalen to"), COMPLEMENT_OF("Complement of"), DISJOINT_WITH(
				"Disjoint with"), DATA_PROPERTY("Data property"), OBJECT_PROPERTY("Object property"), SAME_AS("Same as"), INSTANCE_OF(
				"Instance of"), ANNOTATION_PROPERTY("Annotation Property");

		private final String label;

		ColumnMappingRelationshipType(String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}
	};

	private ColumnMappingRelationshipType type;

	// for object and data properties
	private String uri;

	private ColumnMappingRelationshipBean() {
		// for serialization only
	}

	public ColumnMappingRelationshipBean(ColumnMappingRelationshipType type) {
		this.type = type;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public ColumnMappingRelationshipType getType() {
		return type;
	}

	public void setType(ColumnMappingRelationshipType type) {
		this.type = type;
	}

}

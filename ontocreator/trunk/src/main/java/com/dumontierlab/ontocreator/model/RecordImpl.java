package com.dumontierlab.ontocreator.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RecordImpl implements Record {

	private String id;

	private final Map<String, Field<?>> fields;

	public RecordImpl() {
		this(null);
	}

	public RecordImpl(String _id) {
		fields = new HashMap<String, Field<?>>();
		id = _id;
	}

	public void addField(Field<?> newField) {
		fields.put(newField.getName(), newField);
	}

	public <E> Field<E> addField(String fieldName, E value) {
		Field<E> newField = new FieldImpl<E>(fieldName, value);
		addField(newField);
		return newField;
	}

	public Field<?> getField(String fieldName) {
		return fields.get(fieldName);
	}

	public Set<String> getFieldNames() {

		return fields.keySet();
	}

	public Iterator<Field<?>> getFields() {

		return fields.values().iterator();
	}

	public String getId() {
		return id;
	}

	public void removeField(String fieldId) {
		fields.remove(fieldId);
	}

	public void setId(String _id) {
		id = _id;
	}
}

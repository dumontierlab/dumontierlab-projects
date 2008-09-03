package com.dumontierlab.ontocreator.model;

import java.util.Iterator;
import java.util.Set;

public interface Record {

	void addField(Field<?> newField);

	// get field value by name

	Field<?> getField(String fieldName);

	Set<String> getFieldNames();

	Iterator<Field<?>> getFields();

	String getId(); // format Filename.file position //predefined ID

	void removeField(String fieldId);

}

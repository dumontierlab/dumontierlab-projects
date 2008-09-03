package com.dumontierlab.ontocreator.model;

import java.util.Iterator;
import java.util.Set;

public interface Record {

	Field<?> getField(String fieldName);

	// get field value by name

	Set<String> getFieldNames();

	Iterator<Field<?>> getFields();

	String getId(); // format Filename.file position //predefined ID

}

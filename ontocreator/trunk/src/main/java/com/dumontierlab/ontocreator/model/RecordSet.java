package com.dumontierlab.ontocreator.model;

import java.util.Iterator;

public interface RecordSet extends Iterable<Record> {

	void addRecord(Record newRecord);

	String getId();

	Record getRecord(String recordId);

	Iterator<Record> getRecords();

	int size();

	void removeRecord(String recordId);

}

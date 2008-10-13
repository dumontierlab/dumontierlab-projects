package com.dumontierlab.ontocreator.model;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class RecordSetImpl implements RecordSet {

	private String id;
	private final Map<String, Record> records;

	public RecordSetImpl() {
		this(null);
	}

	public RecordSetImpl(String _id) {
		id = _id;
		records = new LinkedHashMap<String, Record>();
	}

	public void addRecord(Record newRecord) {
		records.put(newRecord.getId(), newRecord);
	}

	public String getId() {
		return id;
	}

	public Record getRecord(String recordId) {
		return records.get(recordId);
	}

	public Iterator<Record> getRecords() {
		return records.values().iterator();
	}

	public int size() {
		return records.size();
	}

	public Iterator<Record> iterator() {
		return getRecords();
	}

	public void removeRecord(String recordId) {
		records.remove(recordId);
	}

	public void setId(String _id) {
		id = _id;
	}
}

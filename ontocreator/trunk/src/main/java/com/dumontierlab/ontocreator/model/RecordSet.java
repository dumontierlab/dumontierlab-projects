package com.dumontierlab.ontocreator.model;

import java.util.Iterator;

public interface RecordSet {
			
	String getId();
	
	int getSize();
	
	Iterator<Record> getRecords();
	
	Record getRecord(String recordId);
	
}

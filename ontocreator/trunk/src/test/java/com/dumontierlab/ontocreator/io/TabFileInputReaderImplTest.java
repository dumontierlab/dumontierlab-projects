package com.dumontierlab.ontocreator.io;

import java.io.InputStream;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.dumontierlab.ontocreator.model.Record;
import com.dumontierlab.ontocreator.model.RecordImpl;
import com.dumontierlab.ontocreator.model.RecordSet;

public class TabFileInputReaderImplTest {

	private TabFileInputReaderImpl reader;
	private InputStream in;

	@Before
	public void setup() {
		in = TabFileInputReaderImplTest.class.getResourceAsStream("/testTabFile.txt");
		reader = new TabFileInputReaderImpl("\t");
	}

	@Test
	public void myTest() {
		RecordSet rset = reader.read(in, true);
		Iterator<Record> recordIterator = rset.getRecords();
		RecordImpl record = (RecordImpl) recordIterator.next();

		System.out.println(record.getId());
		Assert.assertEquals("a", record.getId());

		Assert.assertEquals("a", record.getField("Id").getValue());
		Assert.assertEquals("b", record.getField("Name").getValue());
		Assert.assertEquals("c", record.getField("Address").getValue());
		Assert.assertEquals("d", record.getField("Phone").getValue());

		record = (RecordImpl) recordIterator.next();

		Assert.assertEquals("1", record.getField("Id").getValue());
		Assert.assertEquals("2", record.getField("Name").getValue());
		Assert.assertEquals("3", record.getField("Address").getValue());
		Assert.assertEquals("4", record.getField("Phone").getValue());

		/*
		 * Assert.assertEquals("1", record.getField("0").getValue());
		 * Assert.assertEquals("2", record.getField("1").getValue());
		 * Assert.assertEquals("3", record.getField("2").getValue());
		 * Assert.assertEquals("4", record.getField("3").getValue());
		 */

		System.out.println(record.getId());
		Assert.assertEquals("1", record.getId());

	}
}

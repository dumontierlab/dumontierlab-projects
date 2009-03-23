package com.dumontierlab.ontocreator.ui.client.util;

import java.io.Serializable;

public class DatedResponse<E> implements Serializable {

	private static final long serialVersionUID = 1L;
	private long timestamp;
	private E value;

	private DatedResponse() {
		// for serialization only
	}

	public DatedResponse(E value) {
		this(System.currentTimeMillis(), value);
	}

	public DatedResponse(long timestamp, E value) {
		this.timestamp = timestamp;
		this.value = value;
	}

	public E getValue() {
		return value;
	}

	public long getTimestamp() {
		return timestamp;
	}
}

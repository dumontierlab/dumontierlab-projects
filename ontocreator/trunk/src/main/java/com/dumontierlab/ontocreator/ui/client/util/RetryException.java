package com.dumontierlab.ontocreator.ui.client.util;


public class RetryException extends Exception {

	private int timeout;

	protected RetryException() {
		// for serialization
	}

	public RetryException(int timeout) {
		this.timeout = timeout;
	}

	public int getTimeout() {
		return timeout;
	}

	protected void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}

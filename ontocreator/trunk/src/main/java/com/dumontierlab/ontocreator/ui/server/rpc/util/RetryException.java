package com.dumontierlab.ontocreator.ui.server.rpc.util;

public class RetryException extends Exception {

	private final int timeout;

	public RetryException(int timeout) {
		this.timeout = timeout;
	}

	public int getTimeout() {
		return timeout;
	}

}

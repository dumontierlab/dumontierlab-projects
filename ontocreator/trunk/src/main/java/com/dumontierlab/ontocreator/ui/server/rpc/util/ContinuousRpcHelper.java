package com.dumontierlab.ontocreator.ui.server.rpc.util;

import com.dumontierlab.ontocreator.ui.client.util.RetryException;

public class ContinuousRpcHelper {

	private static final long serialVersionUID = 1L;

	public static void continueLater(int waitBeforeRetry) throws RetryException {
		throw new RetryException(waitBeforeRetry);
	}

}

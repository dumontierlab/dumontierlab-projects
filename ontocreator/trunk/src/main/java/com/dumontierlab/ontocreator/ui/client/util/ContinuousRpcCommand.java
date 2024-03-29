package com.dumontierlab.ontocreator.ui.client.util;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class ContinuousRpcCommand<E> extends AbstractRpcCommand<E> {

	private final Timer retryTimer;

	public ContinuousRpcCommand() {
		this(false);
	}

	public ContinuousRpcCommand(boolean runInBackground) {
		super(runInBackground);
		retryTimer = new Timer() {
			@Override
			public void run() {
				if (!isSuspended() && !isCanceled()) {
					clearRequestOutstanding();
					call();
				}
			}
		};
	}

	@Override
	protected AsyncCallback<E> createCallback() {
		return new AsyncCallback<E>() {
			public void onFailure(Throwable caught) {
				if (caught instanceof RetryException) {
					RetryException retryException = (RetryException) caught;
					if (!isSuspended() && !isCanceled()) {
						retryTimer.schedule(retryException.getTimeout());
					}
				} else {
					clearRequestOutstanding();
					rpcFail(caught);
				}

			}

			public void onSuccess(E result) {
				clearRequestOutstanding();
				rpcReturn(result);
			};
		};
	}

	@Override
	protected void onResume() {
		if (isRequestOutstanding()) {
			retryTimer.cancel();
		}
		call();
	}
}

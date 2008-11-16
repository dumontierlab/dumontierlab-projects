package com.dumontierlab.ontocreator.ui.client.util;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class PeriodicRpcCommand<E> extends AbstractRpcCommand<E> {

	private final Timer timer;
	private final int period;

	public PeriodicRpcCommand(int periodInMillis) {
		this(false, periodInMillis);
	}

	public PeriodicRpcCommand(boolean runInBackground, int periodInMillis) {
		super(runInBackground);

		assert periodInMillis > 0;
		period = periodInMillis;

		timer = new Timer() {
			@Override
			public void run() {
				invokeCallAtSuper();
			}
		};
	}

	@Override
	protected void onResume() {
		if (!isCanceled()) {
			invokeCallAtSuper();
		}
	}

	@Override
	protected void onSuspend() {
		timer.cancel();
	}

	@Override
	protected void onCancel() {
		timer.cancel();
	}

	private void invokeCallAtSuper() {
		super.call();
	}

	@Override
	protected AsyncCallback<E> createCallback() {
		return new AsyncCallback<E>() {
			public void onFailure(Throwable caught) {
				rpcFail(caught);
				clearRequestOutstanding();
			}

			public void onSuccess(E result) {
				rpcReturn(result);
				clearRequestOutstanding();
				if (!isCanceled()) {
					timer.schedule(period);
				}
			};

		};
	}

}

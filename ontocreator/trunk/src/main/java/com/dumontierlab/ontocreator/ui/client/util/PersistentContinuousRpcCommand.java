package com.dumontierlab.ontocreator.ui.client.util;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class PersistentContinuousRpcCommand<E> extends ContinuousRpcCommand<E> {

	public PersistentContinuousRpcCommand() {
		super();
	}

	public PersistentContinuousRpcCommand(boolean runInBackground) {
		super(runInBackground);
	}

	@Override
	protected AsyncCallback<E> createCallback() {
		final AsyncCallback<E> delegate = super.createCallback();
		return new AsyncCallback<E>() {
			public void onFailure(Throwable caught) {
				delegate.onFailure(caught);
			}

			public void onSuccess(E result) {
				delegate.onSuccess(result);
				if (!isSuspended() && !isCanceled()) {
					call(); // persistent calls
				}
			};
		};
	}
}

package com.dumontierlab.ontocreator.ui.client.util;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class OnRequestRpcCommand<E> extends AbstractRpcCommand<E> {

	public OnRequestRpcCommand() {
		super(false);
	}

	@Override
	protected AsyncCallback<E> createCallback() {
		return new AsyncCallback<E>() {
			public void onFailure(Throwable caught) {
				clearRequestOutstanding();
				rpcFail(caught);

			}

			public void onSuccess(E result) {
				clearRequestOutstanding();
				rpcReturn(result);
			};
		};
	}

}

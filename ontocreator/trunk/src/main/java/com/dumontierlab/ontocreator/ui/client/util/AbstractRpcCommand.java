package com.dumontierlab.ontocreator.ui.client.util;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractRpcCommand<E> implements RpcCommand {

	private final AsyncCallback<E> callback;

	public AbstractRpcCommand() {
		callback = new AsyncCallback<E>() {
			public void onFailure(Throwable caught) {
				rpcFail(caught);
			}

			public void onSuccess(E result) {
				rpcReturn(result);
			};
		};
	}

	public void call() {
		rpcCall(callback);
	}

	protected abstract void rpcCall(AsyncCallback<E> callback);

	protected abstract void rpcFail(Throwable caught);

	protected abstract void rpcReturn(E result);

}

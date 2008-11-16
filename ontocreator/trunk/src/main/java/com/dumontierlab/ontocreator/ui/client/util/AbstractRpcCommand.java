package com.dumontierlab.ontocreator.ui.client.util;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractRpcCommand<E> implements RpcCommand {

	private final boolean runInBackground;
	private boolean started;
	private boolean requestOutstanding;
	private boolean canceled;
	private boolean suspended;
	private final AsyncCallback<E> callback;

	public AbstractRpcCommand(boolean runInBackground) {
		this.runInBackground = runInBackground;
		callback = createCallback();
		assert callback != null;
	}

	public void call() {
		if (!requestOutstanding && !canceled) {
			rpcCall(callback);
			requestOutstanding = true;
			started = true;
		}
		assert started;
	}

	public final void cancel() {
		canceled = true;
		requestOutstanding = false;
		onCancel();
	}

	public final void suspend() {
		suspended = true;
		requestOutstanding = false;
		onSuspend();
	}

	public final void resume() {
		suspended = false;
		onResume();
	}

	public boolean isRunInBackground() {
		return runInBackground;
	}

	public boolean isStarted() {
		return started;
	}

	public boolean isRequestOutstanding() {
		return requestOutstanding;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public boolean isCanceled() {
		return canceled;
	}

	protected void clearRequestOutstanding() {
		requestOutstanding = false;
	}

	protected abstract void rpcCall(AsyncCallback<E> callback);

	protected abstract void rpcFail(Throwable caught);

	protected abstract void rpcReturn(E result);

	protected abstract AsyncCallback<E> createCallback();

	protected void onCancel() {
	}

	protected void onSuspend() {

	}

	protected void onResume() {

	}

}

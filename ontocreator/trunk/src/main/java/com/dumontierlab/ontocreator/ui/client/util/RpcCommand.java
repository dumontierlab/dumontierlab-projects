package com.dumontierlab.ontocreator.ui.client.util;

public interface RpcCommand {

	public void call();

	public void suspend();

	public void resume();

	public void cancel();

	public boolean isRunInBackground();

}

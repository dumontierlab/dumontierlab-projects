package com.dumontierlab.ontocreator.ui.client.util;

import java.util.ArrayList;
import java.util.List;

public class RpcCommandPool {

	private final List<RpcCommand> rpcCommandPool = new ArrayList<RpcCommand>();

	public void addRpcCommand(RpcCommand rpcCommand) {
		if (!rpcCommandPool.contains(rpcCommand)) {
			rpcCommandPool.add(rpcCommand);
		}
	}

	public void removeRpcCommand(RpcCommand rpcCommand) {
		rpcCommandPool.remove(rpcCommand);
	}

	public void removeAllRpc() {
		cancelAllRpc();
		rpcCommandPool.clear();
	}

	public void resumeAllRpc() {
		for (RpcCommand rpc : rpcCommandPool) {
			rpc.resume();
		}
	}

	public void suspendAllRpc() {
		for (RpcCommand rpc : rpcCommandPool) {
			rpc.suspend();
		}
	}

	public void cancelAllRpc() {
		for (RpcCommand rpc : rpcCommandPool) {
			rpc.cancel();
		}
	}

	public void suspendAllNonBackgroundRpc() {
		for (RpcCommand rpc : rpcCommandPool) {
			if (!rpc.isRunInBackground()) {
				rpc.suspend();
			}
		}
	}

}

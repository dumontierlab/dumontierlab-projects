package com.dumontierlab.ontocreator.ui.server.rpc.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ContinousRpcServlet extends RemoteServiceServlet {

	private static final String LAST_RESPONSE_SUFFIX = "-lastResponse";

	private final ThreadLocal<RPCRequest> threadLocalPayload = new ThreadLocal<RPCRequest>();

	protected void continueLater(int waitBeforeRetry) throws RetryException {
		throw new RetryException(waitBeforeRetry);
	}

	protected long getLastResponseTimestamp() {
		RPCRequest rpcRequest = threadLocalPayload.get();
		HttpServletRequest request = getThreadLocalRequest();
		Cookie lastAccessCookie = null;
		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(getCookieName(rpcRequest))) {
				lastAccessCookie = cookie;
				break;
			}
		}
		if (lastAccessCookie == null) {
			return 0;
		} else {
			return Long.parseLong(lastAccessCookie.getValue());
		}
	}

	@Override
	protected void onBeforeRequestDeserialized(String serializedRequest) {
		threadLocalPayload.set(RPC.decodeRequest(serializedRequest));
	}

	@Override
	protected void onAfterResponseSerialized(String serializedResponse) {
		RPCRequest rpcRequest = threadLocalPayload.get();
		HttpServletResponse response = getThreadLocalResponse();
		response.addCookie(new Cookie(getCookieName(rpcRequest), Long.toString(System.currentTimeMillis())));
	}

	private String getCookieName(RPCRequest rpcRequest) {
		String methodName = rpcRequest.getMethod().toString();
		int spaceIndex = methodName.lastIndexOf(" ");
		return methodName.substring(spaceIndex + 1) + LAST_RESPONSE_SUFFIX;
	}

}

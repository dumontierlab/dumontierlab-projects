package com.dumontierlab.ontocreator.ui.server.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionHelper {

	private static final String CLIENT_SESSION_ATTRIBUTE_NAME = "Client-Session";

	public static ClientSession getClientSession(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		ClientSession clientSession = (ClientSession) session.getAttribute(CLIENT_SESSION_ATTRIBUTE_NAME);
		if (clientSession == null) {
			clientSession = new ClientSession();
			session.setAttribute(CLIENT_SESSION_ATTRIBUTE_NAME, clientSession);
		}
		return clientSession;

	}

}

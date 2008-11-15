package com.dumontierlab.ontocreator.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionServlet extends HttpServlet {

	public static final String SERVER_ACTION_PARAMETER = "action";
	private final Map<String, HttpCommand> actions = new HashMap<String, HttpCommand>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String actionRequest = req.getParameter(SERVER_ACTION_PARAMETER);
		HttpCommand command = actions.get(actionRequest);
		if (command != null) {
			command.execute(req, resp);
		} else {
			resp
					.sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Invalid Action");
		}
	}

	public void removeAction(String action) {
		actions.remove(action);
	}

	public void addAction(String action, HttpCommand command) {
		actions.put(action, command);
	}

}

package com.dumontierlab.ontocreator.diagnostic;

import javax.servlet.ServletException;

import com.dumontierlab.ontocreator.util.ActionServlet;

public class DiagnosticServlet extends ActionServlet {

	@Override
	public void init() throws ServletException {
		addAction("printClientSessionOntologies", new PrintClientSessionOntologies());
	}

}

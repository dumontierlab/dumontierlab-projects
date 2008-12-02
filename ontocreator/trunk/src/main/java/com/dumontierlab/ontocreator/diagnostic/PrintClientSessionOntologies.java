package com.dumontierlab.ontocreator.diagnostic;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.coode.owl.rdf.rdfxml.RDFXMLRenderer;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.io.RDFXMLOntologyFormat;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.ontocreator.ui.server.session.ClientSession;
import com.dumontierlab.ontocreator.ui.server.session.SessionHelper;
import com.dumontierlab.ontocreator.util.HttpCommand;

public class PrintClientSessionOntologies implements HttpCommand {

	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();

		ClientSession session = SessionHelper.getClientSession(request);
		OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
		for (OWLOntology ontology : session.getInputOntologies()) {
			RDFXMLRenderer renderer = new RDFXMLRenderer(ontologyManager, ontology, writer, new RDFXMLOntologyFormat());
			renderer.render();
			writer.println("-------------------------------------------------------------------");
		}
		writer.flush();
	}
}

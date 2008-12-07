package com.dumontierlab.ontocreator.ui.server.servlet;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.coode.owl.rdf.rdfxml.RDFXMLRenderer;
import org.semanticweb.owl.io.RDFXMLOntologyFormat;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.ontocreator.ui.server.session.ClientSession;
import com.dumontierlab.ontocreator.ui.server.session.SessionHelper;

public class FileDownloadServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ClientSession session = SessionHelper.getClientSession(req);
		OWLOntologyManager manager = null;
		OWLOntology ontology = null;
		String ontologyUri = req.getParameter("uri");
		URI uri = null;
		if (ontologyUri != null) {
			try {
				uri = URI.create(ontologyUri);
			} catch (Exception e) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The specified URI is invalid: " + e.getMessage());
				return;
			}
			manager = session.getInputOntologyManager();
			ontology = manager.getOntology(uri);
		} else {
			manager = session.getOutputOntologyManager();
			ontology = session.getOuputOntology();
		}
		if (ontology == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No ontologies found for your request");
			return;
		}
		sendOntology(manager, ontology, resp);
	}

	private void sendOntology(OWLOntologyManager ontologyManager, OWLOntology ontology, HttpServletResponse resp)
			throws IOException {
		// TODO resp.setContentType("application/rdf+xml");
		resp.setContentType("text/plain");
		Writer writer = resp.getWriter();
		RDFXMLRenderer renderer = new RDFXMLRenderer(ontologyManager, ontology, writer, new RDFXMLOntologyFormat());
		renderer.render();
	}

}

package com.dumontierlab.ontocreator.ui.server.session;

import java.util.Set;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

public class ClientSession {

	private final OWLOntologyManager ontologyManager;

	public ClientSession() {
		ontologyManager = OWLManager.createOWLOntologyManager();
	}

	public OWLOntologyManager getOntologyManager() {
		return ontologyManager;
	}

	public Set<OWLOntology> getOntologies() {
		return ontologyManager.getOntologies();
	}
}

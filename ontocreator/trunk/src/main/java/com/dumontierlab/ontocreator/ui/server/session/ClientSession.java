package com.dumontierlab.ontocreator.ui.server.session;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owl.model.OWLOntology;

public class ClientSession {

	private final Set<OWLOntology> ontologies;

	public ClientSession() {
		ontologies = new HashSet<OWLOntology>();
	}

	public void addOntology(OWLOntology ontology) {
		ontologies.add(ontology);
	}

	public void removeOntology(OWLOntology ontology) {
		ontologies.remove(ontology);
	}

}

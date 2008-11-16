package com.dumontierlab.ontocreator.ui.server.session;

import java.util.Set;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyLoaderListener;
import org.semanticweb.owl.model.OWLOntologyManager;

public class ClientSession implements OWLOntologyLoaderListener {

	private long lastOntologyLoadTimestamp = 0;
	private final OWLOntologyManager ontologyManager;

	public ClientSession() {
		ontologyManager = OWLManager.createOWLOntologyManager();
		ontologyManager.addOntologyLoaderListener(this);
	}

	public OWLOntologyManager getOntologyManager() {
		return ontologyManager;
	}

	public Set<OWLOntology> getOntologies() {
		return ontologyManager.getOntologies();
	}

	public void finishedLoadingOntology(LoadingFinishedEvent event) {
		lastOntologyLoadTimestamp = System.currentTimeMillis();
	}

	public void startedLoadingOntology(LoadingStartedEvent event) {
		// nothing to do
	}
}

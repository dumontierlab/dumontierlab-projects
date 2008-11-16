package com.dumontierlab.ontocreator.ui.server.session;

import java.util.List;
import java.util.Set;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyChangeListener;
import org.semanticweb.owl.model.OWLOntologyManager;

public class ClientSession {

	private long lastOntologyChangeTime;
	private final OWLOntologyManager ontologyManager;

	private ClientSession() {
		ontologyManager = OWLManager.createOWLOntologyManager();
	}

	public static ClientSession newInstance() {
		final ClientSession instance = new ClientSession();
		instance.getOntologyManager().addOntologyChangeListener(new OWLOntologyChangeListener() {
			public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
				instance.lastOntologyChangeTime = System.currentTimeMillis();
			}
		});
		return instance;
	}

	public OWLOntologyManager getOntologyManager() {
		return ontologyManager;
	}

	public Set<OWLOntology> getOntologies() {
		return ontologyManager.getOntologies();
	}

	public long getLastOntologyChangeTime() {
		return lastOntologyChangeTime;
	}

}

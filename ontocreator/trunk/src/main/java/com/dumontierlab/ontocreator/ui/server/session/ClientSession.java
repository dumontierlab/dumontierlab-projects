package com.dumontierlab.ontocreator.ui.server.session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mindswap.pellet.owlapi.PelletReasonerFactory;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyChangeListener;
import org.semanticweb.owl.model.OWLOntologyManager;

public class ClientSession {

	private volatile long lastOntologyChangeTime;
	private final OWLOntologyManager ontologyManager;
	private OWLReasoner reasoner;

	private ClientSession() {
		ontologyManager = OWLManager.createOWLOntologyManager();
	}

	public static ClientSession newInstance() {
		final ClientSession instance = new ClientSession();
		instance.getOntologyManager().addOntologyChangeListener(new OWLOntologyChangeListener() {
			public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
				instance.lastOntologyChangeTime = System.currentTimeMillis();
				synchronized (instance) {
					if (instance.reasoner != null) {
						OWLReasoner reasoner = instance.reasoner;
						synchronized (reasoner) {
							Set<OWLOntology> changedOntologies = new HashSet<OWLOntology>();
							for (OWLOntologyChange change : changes) {
								changedOntologies.add(change.getOntology());
							}
							reasoner.unloadOntologies(changedOntologies);
							reasoner.loadOntologies(changedOntologies);
							reasoner.classify();
						}
					}
				}

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

	public synchronized OWLReasoner getReasoner() throws OWLReasonerException {
		if (reasoner == null) {
			reasoner = new PelletReasonerFactory().createReasoner(ontologyManager);
			synchronized (reasoner) {
				reasoner.loadOntologies(getOntologies());
				reasoner.classify();
			}
		}
		return reasoner;
	}

}

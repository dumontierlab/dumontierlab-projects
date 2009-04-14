package com.dumontierlab.jxta.owl.loader;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.mindswap.pellet.KnowledgeBase;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

public class Loader {

	private final KnowledgeBase kb;
	private final LoaderVisitor visitor;
	private final Set<URI> loadedOntologies;

	public Loader(KnowledgeBase kb) {
		this.kb = kb;
		visitor = new LoaderVisitor(kb);
		loadedOntologies = new HashSet<URI>();
	}

	public void load(OWLOntology ontology, OWLOntologyManager ontologyManager) {
		load(Collections.singleton(ontology), ontologyManager);
	}

	public void load(Set<OWLOntology> ontologies, OWLOntologyManager ontologyManager) {
		for (OWLOntology ontology : ontologies) {
			if (!loadedOntologies.contains(ontology.getURI())) {
				for (OWLOntology ontologyInTheImportClosure : ontologyManager.getImportsClosure(ontology)) {
					if (!loadedOntologies.contains(ontologyInTheImportClosure.getURI())) {
						ontologyInTheImportClosure.accept(visitor);
					}
				}
			}
		}

	}

	public KnowledgeBase getKnowledgeBase() {
		return kb;
	}

	public Set<URI> getLoadedOntologies() {
		return loadedOntologies;
	}
}

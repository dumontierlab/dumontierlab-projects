package com.dumontierlab.ontocreator.engine;

import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyCreationException;

import com.dumontierlab.ontocreator.model.RecordSet;

public interface OntoCreatorEngine {

	public OWLOntology buildInitialOnthology(RecordSet records)
			throws OWLOntologyCreationException, OWLOntologyChangeException;

}

package com.dumontierlab.ontocreator.engine;

import org.semanticweb.owl.model.OWLOntology;

import com.dumontierlab.ontocreator.model.RecordSet;

public interface OntoCreatorEngine {

	public OWLOntology buildInitialOnthology(RecordSet records);

}

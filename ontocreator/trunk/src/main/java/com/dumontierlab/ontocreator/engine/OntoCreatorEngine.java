package com.dumontierlab.ontocreator.engine;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Set;

import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.ontocreator.model.TabFile;
import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingBean;

public interface OntoCreatorEngine {

	public OWLOntology generateOntology(OWLOntology onotlogy, Set<URI> imports, TabFile tabFile,
			Collection<ColumnMappingBean> mappings, OWLOntologyManager ontologyManager)
			throws OWLOntologyCreationException, IOException, OWLOntologyChangeException;

}

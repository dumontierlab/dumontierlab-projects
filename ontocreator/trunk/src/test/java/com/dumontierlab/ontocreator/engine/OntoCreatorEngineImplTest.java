package com.dumontierlab.ontocreator.engine;

import java.io.InputStream;
import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.io.RDFXMLOntologyFormat;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLOntologyStorageException;
import org.semanticweb.owl.model.UnknownOWLOntologyException;

import com.dumontierlab.ontocreator.io.TabFileInputReaderImpl;
import com.dumontierlab.ontocreator.io.TabFileInputReaderImplTest;
import com.dumontierlab.ontocreator.model.RecordSet;

public class OntoCreatorEngineImplTest {

	private TabFileInputReaderImpl reader;
	private InputStream in;
	private OntoCreatorEngineImpl engine;

	@Before
	public void setup() {
		in = TabFileInputReaderImplTest.class
				.getResourceAsStream("/testTabFile.txt");
		reader = new TabFileInputReaderImpl("\t");
		engine = new OntoCreatorEngineImpl();
	}

	@Test
	public void printOntologyTest() throws OWLOntologyCreationException,
			OWLOntologyChangeException, UnknownOWLOntologyException,
			OWLOntologyStorageException {

		RecordSet records = reader.read(in, true);
		OWLOntology ontology = engine.buildInitialOnthology(records);
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		manager.saveOntology(ontology, new RDFXMLOntologyFormat(), URI
				.create("file:///tmp/test.owl"));

	}
}

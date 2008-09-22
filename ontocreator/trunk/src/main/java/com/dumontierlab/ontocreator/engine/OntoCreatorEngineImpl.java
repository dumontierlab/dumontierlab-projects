package com.dumontierlab.ontocreator.engine;

import java.net.URI;
import java.util.Iterator;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.util.SimpleURIMapper;

import com.dumontierlab.ontocreator.model.Field;
import com.dumontierlab.ontocreator.model.Record;
import com.dumontierlab.ontocreator.model.RecordSet;
import com.dumontierlab.ontocreator.util.Constants;

public class OntoCreatorEngineImpl implements OntoCreatorEngine {

	public OWLOntology buildInitialOnthology(RecordSet records)
			throws OWLOntologyCreationException, OWLOntologyChangeException {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		URI ontologyURI = URI.create(Constants.BASE_URI + records.getId());
		URI physicalURI = URI.create("file:/./" + Constants.OUTPUT_DIRECTORY
				+ records.getId() + ".owl");

		SimpleURIMapper mapper = new SimpleURIMapper(ontologyURI, physicalURI);
		manager.addURIMapper(mapper);
		OWLOntology ontology = manager.createOntology(ontologyURI);

		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLClass clsRow = factory.getOWLClass(URI.create(ontologyURI + "#Row"));
		OWLClass clsColumn;
		OWLClass clsFieldName;
		OWLIndividual column;
		OWLIndividual row;

		int rowCounter = 0;
		int fieldCounter = 0;

		Iterator<Record> recordIterator = records.getRecords();
		Iterator<Field<?>> fieldIterator;
		OWLAxiom axiom;
		AddAxiom addAxiom;
		OWLObjectProperty property;

		while (recordIterator.hasNext()) {
			rowCounter++;
			Record r = recordIterator.next();
			row = factory.getOWLIndividual(URI.create(ontologyURI + "#row"
					+ rowCounter));
			axiom = factory.getOWLClassAssertionAxiom(row, clsRow);
			addAxiom = new AddAxiom(ontology, axiom);
			manager.applyChange(addAxiom);

			fieldIterator = r.getFields();
			while (fieldIterator.hasNext()) {
				fieldCounter++;

				Field<?> field = fieldIterator.next();

				clsColumn = factory.getOWLClass(URI.create(ontologyURI
						+ "#Column" + fieldCounter));
				column = factory.getOWLIndividual(URI.create(ontologyURI + "#"
						+ field.getValue()));
				axiom = factory.getOWLClassAssertionAxiom(column, clsColumn);
				addAxiom = new AddAxiom(ontology, axiom);
				manager.applyChange(addAxiom);

				clsFieldName = factory.getOWLClass(URI.create(ontologyURI
						+ "#FieldName" + field.getName()));
				axiom = factory.getOWLSubClassAxiom(clsColumn, clsFieldName);
				addAxiom = new AddAxiom(ontology, axiom);
				manager.applyChange(addAxiom);

				property = factory.getOWLObjectProperty(URI.create(ontologyURI
						+ "#hasColumn"));
				axiom = factory.getOWLObjectPropertyAssertionAxiom(row,
						property, column);

				addAxiom = new AddAxiom(ontology, axiom);
				manager.applyChange(addAxiom);
			}
		}

		return ontology;
	}

}

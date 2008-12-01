package com.dumontierlab.ontocreator.engine;

import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.semanticweb.owl.model.AddAxiom;
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
import com.google.inject.Singleton;

@Singleton
public class OntoCreatorEngineImpl implements OntoCreatorEngine {

	public OWLOntology buildInitialOnthology(RecordSet records, OWLOntologyManager ontologyManager)
			throws OWLOntologyCreationException, OWLOntologyChangeException {

		URI ontologyURI = URI.create(Constants.BASE_URI + records.getId());
		URI physicalURI = URI.create("file:/./" + Constants.OUTPUT_DIRECTORY + records.getId() + ".owl");

		SimpleURIMapper mapper = new SimpleURIMapper(ontologyURI, physicalURI);
		ontologyManager.addURIMapper(mapper);
		OWLOntology ontology = ontologyManager.createOntology(ontologyURI);

		List<AddAxiom> changes = new LinkedList<AddAxiom>();

		OWLDataFactory factory = ontologyManager.getOWLDataFactory();
		OWLClass clsRow = factory.getOWLClass(URI.create(ontologyURI + "#Row"));
		OWLObjectProperty propertyHasColumn = factory.getOWLObjectProperty(URI.create(ontologyURI + "#hasColumn"));

		// add labels for Row class and hasColumn property
		changes.add(new AddAxiom(ontology, factory.getOWLEntityAnnotationAxiom(clsRow, factory
				.getOWLLabelAnnotation("Row"))));
		changes.add(new AddAxiom(ontology, factory.getOWLEntityAnnotationAxiom(propertyHasColumn, factory
				.getOWLLabelAnnotation("hasColumn"))));

		OWLClass clsColumn;
		OWLClass clsFieldName;
		OWLIndividual column;
		OWLIndividual row;

		int rowCounter = 0;
		int columnCounter = 0;

		Iterator<Record> recordIterator = records.getRecords();
		Iterator<Field<?>> fieldIterator;

		while (recordIterator.hasNext()) {
			rowCounter++;
			Record r = recordIterator.next();
			row = factory.getOWLIndividual(URI.create(ontologyURI + "#row" + rowCounter));
			changes.add(new AddAxiom(ontology, factory.getOWLClassAssertionAxiom(row, clsRow)));

			columnCounter = 0;
			fieldIterator = r.getFields();
			while (fieldIterator.hasNext()) {
				columnCounter++;

				Field<?> field = fieldIterator.next();

				clsColumn = factory.getOWLClass(URI.create(ontologyURI + "#Column" + columnCounter));
				column = factory.getOWLIndividual(URI.create(ontologyURI + "#" + field.getValue()));
				changes.add(new AddAxiom(ontology, factory.getOWLClassAssertionAxiom(column, clsColumn)));
				changes.add(new AddAxiom(ontology, factory.getOWLEntityAnnotationAxiom(clsColumn, factory
						.getOWLLabelAnnotation("Column" + columnCounter))));

				clsFieldName = factory.getOWLClass(URI.create(ontologyURI + "#" + field.getName()));
				changes.add(new AddAxiom(ontology, factory.getOWLEntityAnnotationAxiom(clsFieldName, factory
						.getOWLLabelAnnotation(field.getName()))));

				changes.add(new AddAxiom(ontology, factory.getOWLSubClassAxiom(clsColumn, clsFieldName)));

				changes.add(new AddAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(row, propertyHasColumn,
						column)));
			}

		}
		ontologyManager.applyChanges(changes);
		return ontology;
	}
}

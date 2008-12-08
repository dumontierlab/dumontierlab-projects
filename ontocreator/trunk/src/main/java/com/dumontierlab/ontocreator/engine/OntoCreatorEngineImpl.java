package com.dumontierlab.ontocreator.engine;

import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataProperty;
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
import com.dumontierlab.ontocreator.util.Vocabulary;
import com.google.inject.Singleton;

@Singleton
public class OntoCreatorEngineImpl implements OntoCreatorEngine {

	public OWLOntology buildInitialOnthology(RecordSet records, OWLOntologyManager ontologyManager)
			throws OWLOntologyCreationException, OWLOntologyChangeException {

		URI ontologyURI = URI.create(Vocabulary.ONTOCREATOR_NAMESPACE + records.getId()+"/");
		URI physicalURI = URI.create("file:/./" + Constants.OUTPUT_DIRECTORY + records.getId() + ".owl");

		SimpleURIMapper mapper = new SimpleURIMapper(ontologyURI, physicalURI);
		ontologyManager.addURIMapper(mapper);
		OWLOntology ontology = ontologyManager.createOntology(ontologyURI);

		List<AddAxiom> changes = new LinkedList<AddAxiom>();

		OWLDataFactory factory = ontologyManager.getOWLDataFactory();
		OWLClass clsRow = factory.getOWLClass(Vocabulary.ROW.uri());
		OWLObjectProperty propertyHasPart = factory.getOWLObjectProperty(Vocabulary.HAS_PART.uri());
		OWLDataProperty propertyValue = factory.getOWLDataProperty(Vocabulary.VALUE.uri());

		// add labels for Row class and hasColumn property
		changes.add(new AddAxiom(ontology, factory.getOWLEntityAnnotationAxiom(clsRow, factory
				.getOWLLabelAnnotation("Row"))));
		changes.add(new AddAxiom(ontology, factory.getOWLEntityAnnotationAxiom(propertyHasPart, factory
				.getOWLLabelAnnotation("hasPart"))));

		OWLClass clsColumn;
		OWLClass clsFieldName;
		OWLIndividual cell;
		OWLIndividual row;

		int rowCounter = 0;
		int columnCounter = 0;

		Iterator<Record> recordIterator = records.getRecords();
		Iterator<Field<?>> fieldIterator;

		while (recordIterator.hasNext()) {
			rowCounter++;
			Record r = recordIterator.next();
			row = factory.getOWLIndividual(URI.create(ontologyURI + "row_" + rowCounter));
			changes.add(new AddAxiom(ontology, factory.getOWLClassAssertionAxiom(row, clsRow)));

			columnCounter = 0;
			fieldIterator = r.getFields();
			while (fieldIterator.hasNext()) {
				columnCounter++;

				Field<?> field = fieldIterator.next();

				clsColumn = factory.getOWLClass(URI.create(ontologyURI + "Column_" + columnCounter));
				cell = factory.getOWLIndividual(URI.create(ontologyURI + "Cell_"+rowCounter+"_"+columnCounter));
				changes.add(new AddAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(cell, propertyValue, factory.getOWLTypedConstant(field.getValue().toString()))));
				changes.add(new AddAxiom(ontology, factory.getOWLClassAssertionAxiom(cell, clsColumn)));
				changes.add(new AddAxiom(ontology, factory.getOWLEntityAnnotationAxiom(clsColumn, factory
						.getOWLLabelAnnotation("Column" + columnCounter))));

				clsFieldName = factory.getOWLClass(URI.create(ontologyURI + field.getName()));
				changes.add(new AddAxiom(ontology, factory.getOWLEntityAnnotationAxiom(clsFieldName, factory
						.getOWLLabelAnnotation(field.getName()))));

				changes.add(new AddAxiom(ontology, factory.getOWLSubClassAxiom(clsColumn, clsFieldName)));

				changes.add(new AddAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(row, propertyHasPart,
						cell)));
			}

		}
		ontologyManager.applyChanges(changes);
		return ontology;
	}
}

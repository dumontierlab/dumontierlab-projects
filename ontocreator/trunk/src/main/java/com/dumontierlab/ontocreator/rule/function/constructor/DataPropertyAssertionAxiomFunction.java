package com.dumontierlab.ontocreator.rule.function.constructor;

import java.net.URI;

import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.ontocreator.rule.function.RuntimeFunctionException;

public class DataPropertyAssertionAxiomFunction extends AbstractAxiomFunction {

	private final OWLDataProperty property;
	private final String value;

	public DataPropertyAssertionAxiomFunction(OWLOntologyManager manager, OWLOntology outputOntology,
			OWLDataProperty property, String value) {
		super(manager, outputOntology);
		this.property = property;
		this.value = value;
	}

	@Override
	protected OWLAxiom createAxiom(String input) throws RuntimeFunctionException {
		try {
			OWLDataFactory factory = getOWLDataFactory();
			OWLIndividual individual = factory.getOWLIndividual(URI.create(input));
			return factory.getOWLDataPropertyAssertionAxiom(individual, property, value);
		} catch (Exception e) {
			throw new RuntimeFunctionException("Failed to create data property assertion: " + input + " "
					+ property.getURI() + " " + value);
		}
	}

	public String toString(String arg) {
		return property.toString() + "(" + arg + ", " + value + ")";
	}
}

package com.dumontierlab.ontocreator.mapping.function.constructor;

import java.net.URI;

import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.ontocreator.mapping.function.RuntimeFunctionException;

public class ObjectPropertyAssertionAxiomFunction extends AbstractAxiomFunction {

	private final OWLObjectProperty property;
	private final OWLIndividual filler;

	public ObjectPropertyAssertionAxiomFunction(OWLOntologyManager manager, OWLOntology outputOntology,
			OWLObjectProperty property, OWLIndividual filler) {
		super(manager, outputOntology);
		this.property = property;
		this.filler = filler;
	}

	@Override
	protected OWLAxiom createAxiom(String input) throws RuntimeFunctionException {
		try {
			OWLDataFactory factory = getOWLDataFactory();
			OWLIndividual individual = factory.getOWLIndividual(URI.create(input));
			return factory.getOWLObjectPropertyAssertionAxiom(individual, property, filler);
		} catch (Exception e) {
			throw new RuntimeFunctionException("Failed to create object property assertio: " + input + " "
					+ property.getURI() + " " + filler.getURI(), e);
		}
	}

	public String toString(String arg) {
		return property + "(" + arg + ", " + filler + ")";
	}
}

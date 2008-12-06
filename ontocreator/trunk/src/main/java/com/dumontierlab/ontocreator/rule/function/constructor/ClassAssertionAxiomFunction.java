package com.dumontierlab.ontocreator.rule.function.constructor;

import java.net.URI;

import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.ontocreator.rule.function.RuntimeFunctionException;

public class ClassAssertionAxiomFunction extends AbstractAxiomFunction {

	private final OWLDescription concept;

	public ClassAssertionAxiomFunction(OWLOntologyManager manager, OWLOntology outputOntology, OWLDescription concept) {
		super(manager, outputOntology);
		this.concept = concept;
	}

	@Override
	protected OWLAxiom createAxiom(String input) throws RuntimeFunctionException {
		try {
			OWLDataFactory factory = getOWLDataFactory();
			OWLIndividual individual = factory.getOWLIndividual(URI.create(input));
			return factory.getOWLClassAssertionAxiom(individual, concept);
		} catch (Exception e) {
			throw new RuntimeFunctionException("Failed to create class assertion axiom for individual " + input
					+ " and class" + concept, e);
		}
	}

	public String toString(String arg) {
		return concept.toString() + "(" + arg + ")";
	}

}

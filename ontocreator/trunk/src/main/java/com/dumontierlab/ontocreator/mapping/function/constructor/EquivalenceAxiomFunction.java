package com.dumontierlab.ontocreator.mapping.function.constructor;

import java.net.URI;

import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.ontocreator.mapping.function.RuntimeFunctionException;

public class EquivalenceAxiomFunction extends AbstractAxiomFunction {

	private final OWLDescription conceptDescription;

	public EquivalenceAxiomFunction(OWLOntologyManager manager, OWLOntology outputOntology,
			OWLDescription conceptDescription) {
		super(manager, outputOntology);
		this.conceptDescription = conceptDescription;
	}

	@Override
	protected OWLAxiom createAxiom(String input) throws RuntimeFunctionException {
		try {
			OWLDataFactory factory = getOWLDataFactory();
			OWLClass concept = factory.getOWLClass(URI.create(input));
			return factory.getOWLEquivalentClassesAxiom(concept, conceptDescription);
		} catch (Exception e) {
			throw new RuntimeFunctionException("Unable to create equivalent class axiom for " + input + " and "
					+ conceptDescription.toString(), e);
		}

	}

	public String toString(String arg) {
		return "EquivalentClass(" + arg + ", " + conceptDescription + ")";
	}

}

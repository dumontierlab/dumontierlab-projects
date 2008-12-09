package com.dumontierlab.ontocreator.mapping.function.constructor;

import java.net.URI;

import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.ontocreator.mapping.function.RuntimeFunctionException;

public class InclusionAxiomFunction extends AbstractAxiomFunction {

	private final OWLDescription superClass;

	public InclusionAxiomFunction(OWLOntologyManager manager, OWLOntology outputOntology, OWLDescription superClass) {
		super(manager, outputOntology);
		this.superClass = superClass;
	}

	@Override
	public OWLAxiom createAxiom(String input) throws RuntimeFunctionException {
		try {
			OWLDataFactory factory = getOWLDataFactory();
			OWLClass subClass = factory.getOWLClass(URI.create(input));
			return factory.getOWLSubClassAxiom(subClass, superClass);
		} catch (Exception e) {
			throw new RuntimeFunctionException("Failed to create inclusion axiom: " + input + " subclassOf "
					+ superClass.toString(), e);
		}
	}

	public String toString(String arg) {
		return "SubClassOf(" + arg + ", " + superClass + ")";
	}

}

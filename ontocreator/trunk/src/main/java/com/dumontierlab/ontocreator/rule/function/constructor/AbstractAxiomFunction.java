package com.dumontierlab.ontocreator.rule.function.constructor;

import java.util.List;

import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.ontocreator.rule.function.Function;
import com.dumontierlab.ontocreator.rule.function.RuntimeFunctionException;

public abstract class AbstractAxiomFunction implements Function {

	private final OWLOntologyManager manager;
	private final OWLOntology outputOntology;

	public AbstractAxiomFunction(OWLOntologyManager manager, OWLOntology outputOntology) {
		this.manager = manager;
		this.outputOntology = outputOntology;
	}

	public List<String> apply(List<String> input) throws RuntimeFunctionException {
		for (String inputUri : input) {
			try {
				OWLAxiom axiom = createAxiom(inputUri);
				manager.addAxiom(outputOntology, axiom);
			} catch (Exception e) {
				throw new RuntimeFunctionException("Unable to add new axiom to output ontology: "
						+ outputOntology.getURI(), e);
			}
		}
		return input;
	}

	protected OWLOntologyManager getManager() {
		return manager;
	}

	protected OWLOntology getOutputOntology() {
		return outputOntology;
	}

	protected OWLDataFactory getOWLDataFactory() {
		return getManager().getOWLDataFactory();
	}

	protected abstract OWLAxiom createAxiom(String input) throws RuntimeFunctionException;

}

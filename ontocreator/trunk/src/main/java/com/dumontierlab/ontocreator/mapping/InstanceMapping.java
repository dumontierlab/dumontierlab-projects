package com.dumontierlab.ontocreator.mapping;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.ontocreator.mapping.function.RuntimeFunctionException;

public class InstanceMapping extends AbstractMapping{

	private final OWLOntologyManager ontologyManager;

	public InstanceMapping(String name, OWLOntologyManager ontologyManager) {
		super(name);
		this.ontologyManager = ontologyManager;
	}

	public void apply() throws RuntimeFunctionException {
		List<String> input = new ArrayList<String>();
		for(OWLOntology ontology : ontologyManager.getOntologies()){
			for(OWLIndividual individual : ontology.getReferencedIndividuals()){
				input.add(individual.getURI().toString());
			}
		}
		apply(input);
	}

	@Override
	public String toString() {
		return "For all instances X : \n"+super.toString();
	}

}

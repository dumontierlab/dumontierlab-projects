package com.dumontierlab.ontocreator.mapping;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.ontocreator.mapping.function.RuntimeFunctionException;

public class ClassMapping extends AbstractMapping{

	private final OWLOntologyManager ontologyManager;

	public ClassMapping(String mappingName, OWLOntologyManager ontologyManager){
		super(mappingName);
		this.ontologyManager = ontologyManager;
	}

	public void apply() throws RuntimeFunctionException {
		List<String> input = new ArrayList<String>();
		for(OWLOntology ontology : ontologyManager.getOntologies()){
			for(OWLClass clazz : ontology.getReferencedClasses()){
				input.add(clazz.getURI().toString());
			}
		}
		apply(input);
	}

	@Override
	public String toString() {
		return "For all classes X : \n"+super.toString();
	}

}

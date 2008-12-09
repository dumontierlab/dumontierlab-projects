package com.dumontierlab.ontocreator.mapping.function.filter;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;

import com.dumontierlab.ontocreator.mapping.function.Function;
import com.dumontierlab.ontocreator.mapping.function.RuntimeFunctionException;

public class AboxQueryFilter implements Function {

	private final OWLDescription expression;
	private final OWLReasoner reasoner;
	private final OWLDataFactory factory;

	public AboxQueryFilter(OWLReasoner reasoner, OWLDataFactory factory, OWLDescription expression) {
		this.reasoner = reasoner;
		this.factory = factory;
		this.expression = expression;
	}

	public List<String> apply(List<String> input) throws RuntimeFunctionException {
		try {
			List<String> results = new ArrayList<String>();
			Set<OWLIndividual> queryResults = reasoner.getIndividuals(expression, false);
			for (String uri : input) {
				OWLIndividual individual = factory.getOWLIndividual(URI.create(uri));
				if (queryResults.contains(individual)) {
					results.add(individual.getURI().toString());
				}
			}
			return results;
		} catch (Exception e) {
			throw new RuntimeFunctionException("Get indiduals query failed", e);
		}
	}

	public String toString(String arg) {
		return "IF(" + arg + ", instanceOf, " + expression + ")";
	}
}

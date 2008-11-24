package com.dumontierlab.ontocreator.rule.function.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;

import com.dumontierlab.ontocreator.rule.function.Function;
import com.dumontierlab.ontocreator.rule.function.RuntimeFunctionException;

public class AboxQueryFilter implements Function {

	private final OWLDescription expression;
	private final OWLReasoner reasoner;

	public AboxQueryFilter(OWLReasoner reasoner, OWLDescription expression) {
		this.reasoner = reasoner;
		this.expression = expression;
	}

	public List<String> apply(List<String> input) throws RuntimeFunctionException {
		try {
			List<String> results = new ArrayList<String>();
			Set<OWLIndividual> indiduals = reasoner.getIndividuals(expression, false);
			for (OWLIndividual individual : indiduals) {
				results.add(individual.getURI().toString());
			}
			return results;
		} catch (Exception e) {
			throw new RuntimeFunctionException("Get indiduals query failed", e);
		}
	}

}

package com.dumontierlab.ontocreator.rule.function.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;

import com.dumontierlab.ontocreator.rule.function.Function;
import com.dumontierlab.ontocreator.rule.function.RuntimeFunctionException;

public class TboxQueryFilter implements Function {

	public enum QueryType {
		SUBCLASS("SubClassOf"), SUPERCLASS("SuperClassOf"), EQUIVALENT_CLASS("EquivalentClassOf");

		private final String label;

		private QueryType(String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}

	}

	private final OWLDescription expression;
	private final QueryType type;
	private final OWLReasoner reasoner;

	public TboxQueryFilter(OWLReasoner reasoner, OWLDescription expression, QueryType type) {
		this.reasoner = reasoner;
		this.expression = expression;
		this.type = type;
	}

	public List<String> apply(List<String> input) throws RuntimeFunctionException {
		try {
			Set<String> results = new HashSet<String>();
			Set<Set<OWLClass>> classes = null;
			if (type.equals(QueryType.SUBCLASS)) {
				classes = reasoner.getSubClasses(expression);
			} else if (type.equals(QueryType.SUPERCLASS)) {
				classes = reasoner.getSuperClasses(expression);
			} else {
				classes = new HashSet<Set<OWLClass>>();
				classes.add(reasoner.getEquivalentClasses(expression));
			}
			for (Set<OWLClass> equivalentGroup : classes) {
				for (OWLClass clazz : equivalentGroup) {
					results.add(clazz.getURI().toString());
				}
			}
			return new ArrayList<String>(results);
		} catch (Exception e) {
			throw new RuntimeFunctionException("Tbox query failed", e);
		}
	}

	public String toString(String arg) {
		return "IF(" + arg + ", " + type + ", " + expression + ")";
	}
}

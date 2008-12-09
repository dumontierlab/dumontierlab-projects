package com.dumontierlab.ontocreator.mapping.function.filter;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDescription;

import com.dumontierlab.ontocreator.mapping.function.Function;
import com.dumontierlab.ontocreator.mapping.function.RuntimeFunctionException;

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
	private final OWLDataFactory factory;

	public TboxQueryFilter(OWLReasoner reasoner, OWLDataFactory factory, OWLDescription expression, QueryType type) {
		this.reasoner = reasoner;
		this.factory = factory;
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
			for (String uri : input) {
				OWLClass inputClass = factory.getOWLClass(URI.create(uri));
				for (Set<OWLClass> equivalentGroup : classes) {
					if (equivalentGroup.contains(inputClass)) {
						results.add(uri);
						break;
					}
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

package com.dumontierlab.ontocreator.mapping.function.filter;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.ontocreator.mapping.function.Function;
import com.dumontierlab.ontocreator.mapping.function.RuntimeFunctionException;

public class DataPropertySyntaxQueryFilter implements Function {

	private final Pattern regex;
	private final OWLOntologyManager manager;
	private final OWLReasoner reasoner;
	private final OWLDataProperty property;

	public DataPropertySyntaxQueryFilter(OWLOntologyManager manager, OWLReasoner reasoner, OWLDataProperty property,
			Pattern regex) {
		this.manager = manager;
		this.reasoner = reasoner;
		this.property = property;
		this.regex = regex;
	}

	public List<String> apply(List<String> input) throws RuntimeFunctionException {
		try {
			List<String> results = new ArrayList<String>();
			for (String individualUri : input) {
				OWLIndividual individual = manager.getOWLDataFactory().getOWLIndividual(URI.create(individualUri));
				Map<OWLDataProperty, Set<OWLConstant>> properties = reasoner.getDataPropertyRelationships(individual);
				if (properties.containsKey(property)) {
					for (OWLConstant constant : properties.get(property)) {
						if (regex.matcher(constant.getLiteral()).matches()) {
							results.add(individualUri);
							break;
						}
					}
				}

			}
			return results;
		} catch (Exception e) {
			throw new RuntimeFunctionException("Unable to query dataproperty " + property.getURI(), e);
		}
	}

	public String toString(String arg) {
		return "IF(" + arg + ", matches, " + property + "(" + regex + "))";
	}
}

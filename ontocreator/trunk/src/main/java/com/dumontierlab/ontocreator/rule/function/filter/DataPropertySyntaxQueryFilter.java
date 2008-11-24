package com.dumontierlab.ontocreator.rule.function.filter;

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

import com.dumontierlab.ontocreator.rule.function.Function;
import com.dumontierlab.ontocreator.rule.function.RuntimeFunctionException;

public class DataPropertySyntaxQueryFilter implements Function {

	private Pattern regex;
	private OWLOntologyManager manager;
	private OWLReasoner reasoner;
	private OWLDataProperty property;

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
}

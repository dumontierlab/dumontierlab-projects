package com.dumontierlab.ontocreator.ui.server.rpc;

import java.net.URI;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;
import org.coode.manchesterowlsyntax.ManchesterOWLSyntaxDescriptionParser;
import org.semanticweb.owl.expression.ParserException;
import org.semanticweb.owl.expression.ShortFormEntityChecker;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.ontocreator.mapping.BoundMapping;
import com.dumontierlab.ontocreator.mapping.Mapping;
import com.dumontierlab.ontocreator.mapping.function.RuntimeFunctionException;
import com.dumontierlab.ontocreator.mapping.function.constructor.ClassAssertionAxiomFunction;
import com.dumontierlab.ontocreator.mapping.function.filter.AboxQueryFilter;
import com.dumontierlab.ontocreator.mapping.function.filter.DataPropertySyntaxQueryFilter;
import com.dumontierlab.ontocreator.mapping.function.filter.TboxQueryFilter;
import com.dumontierlab.ontocreator.mapping.function.filter.TboxQueryFilter.QueryType;
import com.dumontierlab.ontocreator.ui.client.rpc.RuleService;
import com.dumontierlab.ontocreator.ui.client.rpc.exception.NoOutputOntologyException;
import com.dumontierlab.ontocreator.ui.client.rpc.exception.RuleServiceException;
import com.dumontierlab.ontocreator.ui.server.session.ClientSession;
import com.dumontierlab.ontocreator.ui.server.session.SessionHelper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RuleServiceImpl extends RemoteServiceServlet implements RuleService {

	private static final Logger LOG = Logger.getLogger(RuleServiceImpl.class);

	public String createInstanceMapping() throws RuleServiceException {
		ClientSession session = SessionHelper.getClientSession(getThreadLocalRequest());
		Mapping mapping = session.createInstanceMapping();
		session.addMapping(mapping);
		return mapping.getName();
	}

	public String createBoundMapping(String uri) throws RuleServiceException {
		ClientSession session = SessionHelper.getClientSession(getThreadLocalRequest());
		BoundMapping mapping = session.createBoundMapping(uri);
		session.addMapping(mapping);
		return mapping.getName();
	}

	public String createClassMapping() throws RuleServiceException {
		ClientSession session = SessionHelper.getClientSession(getThreadLocalRequest());
		Mapping mapping = session.createClassMapping();
		session.addMapping(mapping);
		return mapping.getName();
	}

	public String addABoxQueryFilter(String ruleName, String query) throws NoOutputOntologyException,
			RuleServiceException {
		ClientSession session = SessionHelper.getClientSession(getThreadLocalRequest());
		Mapping rule = getRule(session, ruleName);
		ManchesterOWLSyntaxDescriptionParser parser = new ManchesterOWLSyntaxDescriptionParser(session
				.getInputOntologyManager().getOWLDataFactory(), new ShortFormEntityChecker(session
				.getBidirectionalShortFormProvider()));
		try {
			OWLDescription description = parser.parse(query);
			rule.add(new AboxQueryFilter(session.getInputReasoner(), session.getInputOntologyManager()
					.getOWLDataFactory(), description));
		} catch (ParserException e) {
			error("Invalid expression: " + query + ". Expecting expression in Manchester OWL Syntax", e);
		} catch (OWLReasonerException e) {
			error("Reasoner exception: " + e.getMessage(), e);
		}
		return rule.toString();
	}

	public String addTBoxQueryFilter(String ruleName, String queryType, String query) throws RuleServiceException {
		ClientSession session = SessionHelper.getClientSession(getThreadLocalRequest());
		Mapping rule = getRule(session, ruleName);
		QueryType queryTypeValue = QueryType.valueOf(QueryType.class, queryType);
		if (queryType == null) {
			throw new RuleServiceException("Invalid query type (" + queryType + ")");
		}
		ManchesterOWLSyntaxDescriptionParser parser = new ManchesterOWLSyntaxDescriptionParser(session
				.getInputOntologyManager().getOWLDataFactory(), new ShortFormEntityChecker(session
				.getBidirectionalShortFormProvider()));
		try {
			OWLDescription description = parser.parse(query);
			rule.add(new TboxQueryFilter(session.getInputReasoner(), session.getInputOntologyManager()
					.getOWLDataFactory(), description, queryTypeValue));

		} catch (ParserException e) {
			error("Invalid expression: " + query + ". Expecting expression in Manchester OWL Syntax", e);
		} catch (OWLReasonerException e) {
			error("Reasoner exception: " + e.getMessage(), e);
		}
		return rule.toString();
	}

	public String addDataPropertyRegex(String ruleName, String propertyUri, String regex) throws RuleServiceException {
		ClientSession session = SessionHelper.getClientSession(getThreadLocalRequest());
		Mapping rule = getRule(session, ruleName);

		try {
			Pattern regexPattern = Pattern.compile(regex);
			URI uri = URI.create(propertyUri);
			OWLOntologyManager manager = session.getInputOntologyManager();
			OWLDataProperty property = manager.getOWLDataFactory().getOWLDataProperty(uri);
			rule.add(new DataPropertySyntaxQueryFilter(manager, session.getInputReasoner(), property, regexPattern));

		} catch (PatternSyntaxException e) {
			error("Invalid regular expression (" + regex + ")", e);
		} catch (IllegalArgumentException e) {
			error("Invalid data property URI (" + propertyUri + ")", e);
		} catch (OWLReasonerException e) {
			error("Reasoner exception: " + e.getMessage(), e);
		}
		return rule.toString();

	}

	public String addClassAssertion(String ruleName, String description) throws RuleServiceException {
		ClientSession session = SessionHelper.getClientSession(getThreadLocalRequest());
		assertOutputOntologyIsCreated(session);
		Mapping rule = getRule(session, ruleName);
		ManchesterOWLSyntaxDescriptionParser parser = new ManchesterOWLSyntaxDescriptionParser(session
				.getInputOntologyManager().getOWLDataFactory(), new ShortFormEntityChecker(session
				.getBidirectionalShortFormProvider()));
		try {
			OWLDescription owlDescription = parser.parse(description);
			rule.add(new ClassAssertionAxiomFunction(session.getOutputOntologyManager(), session.getOuputOntology(),
					owlDescription));

		} catch (ParserException e) {
			error("Invalid expression: " + description + ". Expecting expression in Manchester OWL Syntax", e);
		}
		return rule.toString();
	}

	public void apply(String ruleName) throws RuleServiceException {
		ClientSession session = SessionHelper.getClientSession(getThreadLocalRequest());
		assertOutputOntologyIsCreated(session);
		Mapping rule = getRule(session, ruleName);
		try {
			rule.apply();
		} catch (RuntimeFunctionException e) {
			error("Failed to apply rule (" + ruleName + "): " + e.getMessage(), e);
		}
	}

	/* ---------------------------- helper methods -- */
	private void assertOutputOntologyIsCreated(ClientSession session) throws NoOutputOntologyException {
		if (session.getOuputOntology() == null) {
			throw new NoOutputOntologyException(
					"You need to create an output ontology. You can do so from the File menu.");
		}
	}

	private Mapping getRule(ClientSession session, String ruleName) throws RuleServiceException {
		Mapping rule = session.getMapping(ruleName);
		if (rule == null) {
			throw new RuleServiceException("Rule " + ruleName + " does not exists on this session");
		}
		return rule;
	}

	private void error(String message, Throwable cause) throws RuleServiceException {
		LOG.error(message, cause);
		throw new RuleServiceException(message);
	}

}

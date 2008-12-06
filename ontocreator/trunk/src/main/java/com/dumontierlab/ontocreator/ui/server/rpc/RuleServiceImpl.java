package com.dumontierlab.ontocreator.ui.server.rpc;

import org.coode.manchesterowlsyntax.ManchesterOWLSyntaxDescriptionParser;
import org.semanticweb.owl.expression.ParserException;
import org.semanticweb.owl.expression.ShortFormEntityChecker;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLDescription;

import com.dumontierlab.ontocreator.rule.Rule;
import com.dumontierlab.ontocreator.rule.function.RuntimeFunctionException;
import com.dumontierlab.ontocreator.rule.function.constructor.ClassAssertionAxiomFunction;
import com.dumontierlab.ontocreator.rule.function.filter.AboxQueryFilter;
import com.dumontierlab.ontocreator.ui.client.rpc.RuleService;
import com.dumontierlab.ontocreator.ui.client.rpc.exception.NoOutputOntologyException;
import com.dumontierlab.ontocreator.ui.client.rpc.exception.RuleServiceException;
import com.dumontierlab.ontocreator.ui.server.session.ClientSession;
import com.dumontierlab.ontocreator.ui.server.session.SessionHelper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RuleServiceImpl extends RemoteServiceServlet implements RuleService {

	public void createRule(String name) throws RuleServiceException {
		ClientSession session = SessionHelper.getClientSession(getThreadLocalRequest());
		if (session.getRule(name) != null) {
			throw new RuleServiceException("A rule with this name [" + name + "] already exists on this session.");
		}
		Rule rule = new Rule(name);
		session.addRule(rule);
	}

	public String addABoxQueryFilter(String ruleName, String query) throws NoOutputOntologyException,
			RuleServiceException {
		ClientSession session = SessionHelper.getClientSession(getThreadLocalRequest());
		Rule rule = getRule(session, ruleName);
		ManchesterOWLSyntaxDescriptionParser parser = new ManchesterOWLSyntaxDescriptionParser(session
				.getInputOntologyManager().getOWLDataFactory(), new ShortFormEntityChecker(session
				.getBidirectionalShortFormProvider()));
		try {
			OWLDescription description = parser.parse(query);
			rule.add(new AboxQueryFilter(session.getInputReasoner(), description));
			return rule.toString();
		} catch (ParserException e) {
			throw new RuleServiceException("Invalid expression: " + query
					+ ". Expecting expression in Manchester OWL Syntax", e);
		} catch (OWLReasonerException e) {
			throw new RuleServiceException("Reasoner exception: " + e.getMessage(), e);
		}
	}

	public String addClassAssertion(String ruleName, String description) throws RuleServiceException {
		ClientSession session = SessionHelper.getClientSession(getThreadLocalRequest());
		Rule rule = getRule(session, ruleName);
		ManchesterOWLSyntaxDescriptionParser parser = new ManchesterOWLSyntaxDescriptionParser(session
				.getInputOntologyManager().getOWLDataFactory(), new ShortFormEntityChecker(session
				.getBidirectionalShortFormProvider()));
		try {
			OWLDescription owlDescription = parser.parse(description);
			rule.add(new ClassAssertionAxiomFunction(session.getInputOntologyManager(), session.getOuputOntology(),
					owlDescription));
			return rule.toString();
		} catch (ParserException e) {
			throw new RuleServiceException("Invalid expression: " + description
					+ ". Expecting expression in Manchester OWL Syntax", e);
		}
	}

	public void apply(String ruleName) throws RuleServiceException {
		ClientSession session = SessionHelper.getClientSession(getThreadLocalRequest());
		Rule rule = getRule(session, ruleName);
		try {
			rule.apply();
		} catch (RuntimeFunctionException e) {
			throw new RuleServiceException("Failed to apply rule (" + ruleName + "): " + e.getMessage(), e);
		}
	}

	/* ---------------------------- helper methods -- */
	private void assertOutputOntologyIsCreated(ClientSession session) throws NoOutputOntologyException {
		if (session.getOuputOntology() == null) {
			throw new NoOutputOntologyException();
		}
	}

	private Rule getRule(ClientSession session, String ruleName) throws RuleServiceException {
		Rule rule = session.getRule(ruleName);
		if (rule == null) {
			throw new RuleServiceException("Rule " + ruleName + " does not exists on this session");
		}
		return rule;
	}

}

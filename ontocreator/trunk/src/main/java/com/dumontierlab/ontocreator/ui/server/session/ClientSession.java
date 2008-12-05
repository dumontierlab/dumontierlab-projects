package com.dumontierlab.ontocreator.ui.server.session;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.mindswap.pellet.owlapi.PelletReasonerFactory;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyChangeListener;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.util.BidirectionalShortFormProvider;

import com.dumontierlab.ontocreator.rule.Rule;
import com.dumontierlab.ontocreator.util.OntoCreatorBidirectionalShortFormProvider;

public class ClientSession {

	private volatile long lastInputOntologyChangeTime;
	private volatile long lastOutputOntologyChangeTime;
	private final OWLOntologyManager inputOntologyManager;
	private final OWLOntologyManager outputOntologyManager;
	private final OntoCreatorBidirectionalShortFormProvider shortFormProvider;
	private OWLOntology outputOntology;
	private OWLReasoner inputReasoner;
	private final Map<String, Rule> rules;

	private ClientSession() {
		rules = new ConcurrentHashMap<String, Rule>();
		inputOntologyManager = OWLManager.createOWLOntologyManager();
		outputOntologyManager = OWLManager.createOWLOntologyManager();
		shortFormProvider = new OntoCreatorBidirectionalShortFormProvider(inputOntologyManager);
	}

	public static ClientSession newInstance() {
		final ClientSession instance = new ClientSession();
		instance.getInputOntologyManager().addOntologyChangeListener(new OWLOntologyChangeListener() {
			public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
				instance.lastInputOntologyChangeTime = System.currentTimeMillis();
				synchronized (instance) {
					if (instance.inputReasoner != null) {
						OWLReasoner reasoner = instance.inputReasoner;
						synchronized (reasoner) {
							Set<OWLOntology> changedOntologies = new HashSet<OWLOntology>();
							for (OWLOntologyChange change : changes) {
								changedOntologies.add(change.getOntology());
							}
							reasoner.unloadOntologies(changedOntologies);
							reasoner.loadOntologies(changedOntologies);
							reasoner.classify();
						}
					}
				}
			}
		});
		instance.getOutputOntologyManager().addOntologyChangeListener(new OWLOntologyChangeListener() {
			public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
				instance.lastOutputOntologyChangeTime = System.currentTimeMillis();
			}
		});
		return instance;
	}

	public OWLOntologyManager getInputOntologyManager() {
		return inputOntologyManager;
	}

	public OWLOntologyManager getOutputOntologyManager() {
		return outputOntologyManager;
	}

	public BidirectionalShortFormProvider getBidirectionalShortFormProvider() {
		return shortFormProvider;
	}

	public Set<OWLOntology> getInputOntologies() {
		return inputOntologyManager.getOntologies();
	}

	public Set<OWLOntology> getOutputOntologies() {
		return outputOntologyManager.getOntologies();
	}

	public long getLastInputOntologyChangeTime() {
		return lastInputOntologyChangeTime;
	}

	public long getLastOutputOntologyChangeTime() {
		return lastOutputOntologyChangeTime;
	}

	public synchronized OWLReasoner getInputReasoner() throws OWLReasonerException {
		if (inputReasoner == null) {
			inputReasoner = new PelletReasonerFactory().createReasoner(inputOntologyManager);
			synchronized (inputReasoner) {
				inputReasoner.loadOntologies(getInputOntologies());
				inputReasoner.classify();
			}
		}
		return inputReasoner;
	}

	public void createOutputOntology(URI uri) throws OWLOntologyCreationException {
		outputOntology = outputOntologyManager.createOntology(uri);
	}

	public OWLOntology getOuputOntology() {
		return outputOntology;
	}

	public void addRule(Rule rule) {
		rules.put(rule.getName(), rule);
	}

	public void removeRule(String name) {
		rules.remove(name);
	}

	public Rule getRule(String name) {
		return rules.get(name);
	}

	public Set<Rule> getRules() {
		return new HashSet<Rule>(rules.values());
	}

	public boolean containsRule(String ruleName) {
		return rules.containsKey(ruleName);
	}

}

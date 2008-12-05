package com.dumontierlab.ontocreator.ui.server.rpc;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLProperty;

import com.clarkparsia.owlapi.OWL;
import com.dumontierlab.ontocreator.ui.client.model.OWLClassBean;
import com.dumontierlab.ontocreator.ui.client.model.OWLIndividualBean;
import com.dumontierlab.ontocreator.ui.client.model.OWLPropertyBean;
import com.dumontierlab.ontocreator.ui.client.model.TreeNode;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.dumontierlab.ontocreator.ui.client.util.RetryException;
import com.dumontierlab.ontocreator.ui.server.rpc.util.ContinousRpcServlet;
import com.dumontierlab.ontocreator.ui.server.session.ClientSession;
import com.dumontierlab.ontocreator.ui.server.session.SessionHelper;

public class OntologyServiceImpl extends ContinousRpcServlet implements OntologyService {

	private static final int RETRY_TIME = 3000;

	public Set<String> getLoadedOntologies() throws RetryException {
		ClientSession session = getClientSession();

		if (getLastResponseTimestamp() >= session.getLastInputOntologyChangeTime()) {
			continueLater(RETRY_TIME);
		}

		Set<String> ontologyUris = new HashSet<String>();
		for (OWLOntology ontology : session.getInputOntologies()) {
			ontologyUris.add(ontology.getURI().toString());
		}

		return ontologyUris;
	}

	public void createOutputOntology(String uri) {
		try {
			getClientSession().createOutputOntology(URI.create(uri));
		} catch (Exception e) {
			// TODO:
		}
	}

	public String getOutputOntology() throws RetryException {
		ClientSession session = getClientSession();

		if (getLastResponseTimestamp() >= session.getLastOutputOntologyChangeTime()) {
			continueLater(RETRY_TIME);
		}

		return session.getOuputOntology() == null ? null : session.getOuputOntology().getURI().toString();
	}

	public TreeNode<OWLClassBean> getClassHierarchy() {
		ClientSession session = getClientSession();
		TreeNode<OWLClassBean> tree = null;
		try {
			OWLReasoner reasoner = session.getInputReasoner();
			synchronized (reasoner) {
				tree = createTaxonomyTree(OWL.Thing, reasoner);
			}
		} catch (OWLReasonerException e) {
			throw new RuntimeException("Ontology classification failed.", e);
		}
		return tree;
	}

	public TreeNode<OWLPropertyBean> getPropertyHierarchy() {
		ClientSession session = getClientSession();

		TreeNode<OWLPropertyBean> root = new TreeNode<OWLPropertyBean>(null);
		for (OWLOntology ontology : session.getInputOntologies()) {
			for (OWLObjectProperty objectProperty : ontology.getReferencedObjectProperties()) {
				root.addChild(new TreeNode<OWLPropertyBean>(createOWLPropertyBean(getShortForm(session, objectProperty),
						objectProperty, ontology)));
			}
			for (OWLDataProperty dataProperty : ontology.getReferencedDataProperties()) {
				root.addChild(new TreeNode<OWLPropertyBean>(createOWLPropertyBean(getShortForm(session, dataProperty),
						dataProperty, ontology)));
			}
		}

		return root;
	}

	public List<OWLIndividualBean> getIndividuals() {
		ClientSession session = getClientSession();
		List<OWLIndividualBean> results = new ArrayList<OWLIndividualBean>();

		for (OWLOntology ontology : session.getInputOntologies()) {
			for (OWLIndividual individual : ontology.getReferencedIndividuals()) {
				if (!results.contains(individual)) {
					results.add(createOWLIndividualBean(getShortForm(session, individual), individual, ontology));
				}
			}
		}
		return results;
	}

	private OWLPropertyBean createOWLPropertyBean(String shortForm, OWLProperty<?, ?> property, OWLOntology ontology) {
		OWLPropertyBean prop = new OWLPropertyBean();
		prop.setUri(property.getURI().toString());
		prop.setLabel(shortForm);
		return prop;
	}

	private OWLIndividualBean createOWLIndividualBean(String shortFrom, OWLIndividual individual, OWLOntology ontology) {
		OWLIndividualBean individualBean = new OWLIndividualBean();
		individualBean.setUri(individual.getURI().toString());
		individualBean.setLabel(shortFrom);
		return individualBean;
	}

	private TreeNode<OWLClassBean> createTaxonomyTree(OWLClass concept, OWLReasoner reasoner)
			throws OWLReasonerException {
		OWLClass owlNothing = OWL.Nothing;
		TreeNode<OWLClassBean> root = new TreeNode<OWLClassBean>(createOWLClassBean(getShortForm(concept), concept,
				reasoner));
		for (Set<OWLClass> children : reasoner.getSubClasses(concept)) {
			for (OWLClass subclass : children) {
				if (!subclass.equals(concept) && !subclass.equals(owlNothing)) {
					TreeNode<OWLClassBean> node = createTaxonomyTree(subclass, reasoner);
					root.addChild(node);
				}
			}
		}
		return root;
	}

	private OWLClassBean createOWLClassBean(String shortForm, OWLClass concept, OWLReasoner reasoner)
			throws OWLReasonerException {
		OWLClassBean classBean = new OWLClassBean();
		classBean.setUri(concept.getURI().toString());
		classBean.setLabel(shortForm);
		classBean.setUnsatisfiable(!reasoner.isSatisfiable(concept));
		return classBean;
	}

	private ClientSession getClientSession() {
		HttpServletRequest request = getThreadLocalRequest();
		return SessionHelper.getClientSession(request);
	}

	private String getShortForm(OWLEntity entity) {
		return getShortForm(getClientSession(), entity);
	}

	private String getShortForm(ClientSession session, OWLEntity entity) {
		return session.getBidirectionalShortFormProvider().getShortForm(entity);
	}

}

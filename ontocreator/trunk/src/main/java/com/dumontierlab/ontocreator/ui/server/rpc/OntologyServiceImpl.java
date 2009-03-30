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
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLProperty;

import com.clarkparsia.owlapi.OWL;
import com.dumontierlab.ontocreator.ui.client.model.OWLClassBean;
import com.dumontierlab.ontocreator.ui.client.model.OWLIndividualBean;
import com.dumontierlab.ontocreator.ui.client.model.OWLPropertyBean;
import com.dumontierlab.ontocreator.ui.client.model.TreeNode;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.dumontierlab.ontocreator.ui.client.rpc.exception.ServiceException;
import com.dumontierlab.ontocreator.ui.client.util.DatedResponse;
import com.dumontierlab.ontocreator.ui.client.util.RetryException;
import com.dumontierlab.ontocreator.ui.server.rpc.util.ContinuousRpcHelper;
import com.dumontierlab.ontocreator.ui.server.session.ClientSession;
import com.dumontierlab.ontocreator.ui.server.session.SessionHelper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class OntologyServiceImpl extends RemoteServiceServlet implements OntologyService {

	private static final long serialVersionUID = 1L;
	private static final int RETRY_TIME = 3000;

	public void loadOntology(String physicalUri) throws ServiceException {
		try {
			URI uri = URI.create(physicalUri);
			getClientSession().getInputOntologyManager().loadOntologyFromPhysicalURI(uri);
		} catch (IllegalArgumentException e) {
			throw new ServiceException("This URI is invald (" + physicalUri + ")", e);
		} catch (OWLOntologyCreationException e) {
			throw new ServiceException("Unable o load ontology: " + e.getMessage(), e);
		}
	}

	public DatedResponse<Set<String>> getLoadedOntologies(long lastUpdate) throws RetryException {
		ClientSession session = getClientSession();

		if (lastUpdate >= session.getLastInputOntologyChangeTime()) {
			ContinuousRpcHelper.continueLater(RETRY_TIME);
		}

		Set<String> ontologyUris = new HashSet<String>();
		for (OWLOntology ontology : session.getInputOntologies()) {
			ontologyUris.add(ontology.getURI().toString());
		}

		return new DatedResponse<Set<String>>(ontologyUris);
	}

	public void createOutputOntology(String uri) {
		try {
			getClientSession().createOutputOntology(URI.create(uri));
		} catch (Exception e) {
			// TODO:
		}
	}

	public DatedResponse<String> getOutputOntology(long lastUpdate) throws RetryException {
		ClientSession session = getClientSession();

		if (lastUpdate >= session.getLastOutputOntologyChangeTime()) {
			ContinuousRpcHelper.continueLater(RETRY_TIME);
		}

		DatedResponse<String> response = new DatedResponse<String>(session.getOuputOntology() == null ? null : session
				.getOuputOntology().getURI().toString());
		return response;
	}

	public TreeNode<OWLClassBean> getInputClassHierarchy() {
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

	public TreeNode<OWLPropertyBean> getInputPropertyHierarchy() {
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

	public List<OWLPropertyBean> getInputObjectProperties() {
		ClientSession session = getClientSession();

		List<OWLPropertyBean> result = new ArrayList<OWLPropertyBean>();
		for (OWLOntology ontology : session.getInputOntologies()) {
			for (OWLObjectProperty objectProperty : ontology.getReferencedObjectProperties()) {
				result.add(createOWLPropertyBean(getShortForm(session, objectProperty), objectProperty, ontology));
			}
		}
		return result;
	}

	public List<OWLPropertyBean> getInputDataProperties() {
		ClientSession session = getClientSession();

		List<OWLPropertyBean> result = new ArrayList<OWLPropertyBean>();
		for (OWLOntology ontology : session.getInputOntologies()) {
			for (OWLDataProperty dataProperty : ontology.getReferencedDataProperties()) {
				result.add(createOWLPropertyBean(getShortForm(session, dataProperty), dataProperty, ontology));
			}
		}
		return result;
	}

	public List<OWLIndividualBean> getInputIndividuals() {
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

	public TreeNode<OWLClassBean> getOutputClassHierarchy() {
		ClientSession session = getClientSession();
		if (session.getOuputOntology() == null) {
			return null;
		}
		TreeNode<OWLClassBean> tree = null;
		try {
			OWLReasoner reasoner = session.getOutputReasoner();
			synchronized (reasoner) {
				tree = createTaxonomyTree(OWL.Thing, reasoner);
			}
		} catch (OWLReasonerException e) {
			throw new RuntimeException("Ontology classification failed.", e);
		}
		return tree;
	}

	public List<OWLIndividualBean> getOutputIndividuals() {
		ClientSession session = getClientSession();
		OWLOntology ontology = session.getOuputOntology();
		if (ontology == null) {
			return null;
		}

		List<OWLIndividualBean> results = new ArrayList<OWLIndividualBean>();
		for (OWLIndividual individual : ontology.getReferencedIndividuals()) {
			if (!results.contains(individual)) {
				results.add(createOWLIndividualBean(getShortForm(session, individual), individual, ontology));
			}
		}

		return results;
	}

	public TreeNode<OWLPropertyBean> getOutputPropertyHierarchy() {
		ClientSession session = getClientSession();
		OWLOntology ontology = session.getOuputOntology();
		if (ontology == null) {
			return null;
		}

		TreeNode<OWLPropertyBean> root = new TreeNode<OWLPropertyBean>(null);
		for (OWLObjectProperty objectProperty : ontology.getReferencedObjectProperties()) {
			root.addChild(new TreeNode<OWLPropertyBean>(createOWLPropertyBean(getShortForm(session, objectProperty),
					objectProperty, ontology)));
		}
		for (OWLDataProperty dataProperty : ontology.getReferencedDataProperties()) {
			root.addChild(new TreeNode<OWLPropertyBean>(createOWLPropertyBean(getShortForm(session, dataProperty),
					dataProperty, ontology)));
		}

		return root;
	}

	public List<OWLClassBean> getInputClasses() {
		ClientSession session = getClientSession();
		List<OWLClassBean> results = new ArrayList<OWLClassBean>();

		for (OWLOntology ontology : session.getInputOntologies()) {
			for (OWLClass concept : ontology.getReferencedClasses()) {
				if (!results.contains(concept)) {
					try {
						results.add(createOWLClassBean(getShortForm(session, concept), concept, session
								.getInputReasoner()));
					} catch (OWLReasonerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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

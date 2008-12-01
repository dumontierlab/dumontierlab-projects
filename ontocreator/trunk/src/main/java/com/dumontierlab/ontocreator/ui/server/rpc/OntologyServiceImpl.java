package com.dumontierlab.ontocreator.ui.server.rpc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLProperty;

import uk.ac.manchester.owl.tutorial.LabelExtractor;

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
import com.dumontierlab.ontocreator.util.RenderingHelper;

public class OntologyServiceImpl extends ContinousRpcServlet implements OntologyService {

	private static final int RETRY_TIME = 3000;

	public Set<String> getLoadedOntologies() throws RetryException {
		ClientSession session = getClientSession();

		if (getLastResponseTimestamp() >= session.getLastOntologyChangeTime()) {
			continueLater(RETRY_TIME);
		}

		Set<String> ontologyUris = new HashSet<String>();
		for (OWLOntology ontology : session.getOntologies()) {
			ontologyUris.add(ontology.getURI().toString());
		}

		return ontologyUris;
	}

	public TreeNode<OWLClassBean> getClassHierarchy() {
		ClientSession session = getClientSession();
		TreeNode<OWLClassBean> tree = null;
		try {
			OWLReasoner reasoner = session.getReasoner();
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
		for (OWLOntology ontology : session.getOntologies()) {
			for (OWLObjectProperty objectProperty : ontology.getReferencedObjectProperties()) {
				root.addChild(new TreeNode<OWLPropertyBean>(createOWLPropertyBean(objectProperty, ontology)));
			}
			for (OWLDataProperty dataProperty : ontology.getReferencedDataProperties()) {
				root.addChild(new TreeNode<OWLPropertyBean>(createOWLPropertyBean(dataProperty, ontology)));
			}
		}

		return root;
	}

	public List<OWLIndividualBean> getIndividuals() {
		ClientSession session = getClientSession();
		List<OWLIndividualBean> results = new ArrayList<OWLIndividualBean>();

		for (OWLOntology ontology : session.getOntologies()) {
			for (OWLIndividual individual : ontology.getReferencedIndividuals()) {
				if (!results.contains(individual)) {
					results.add(createOWLIndividualBean(individual, ontology));
				}
			}
		}
		return results;
	}

	private OWLPropertyBean createOWLPropertyBean(OWLProperty<?, ?> property, OWLOntology ontology) {
		OWLPropertyBean prop = new OWLPropertyBean();
		prop.setUri(property.getURI().toString());
		String label = getLabel(property, ontology);
		prop.setLabel(label == null ? RenderingHelper.getLabelFromUri(property.getURI()) : label);
		return prop;
	}

	private OWLIndividualBean createOWLIndividualBean(OWLIndividual individual, OWLOntology ontology) {
		OWLIndividualBean individualBean = new OWLIndividualBean();
		individualBean.setUri(individual.getURI().toString());
		String label = getLabel(individual, ontology);
		individualBean.setLabel(label == null ? RenderingHelper.getLabelFromUri(individual.getURI()) : label);
		return individualBean;
	}

	private TreeNode<OWLClassBean> createTaxonomyTree(OWLClass concept, OWLReasoner reasoner)
			throws OWLReasonerException {
		OWLClass owlNothing = OWL.Nothing;
		TreeNode<OWLClassBean> root = new TreeNode<OWLClassBean>(createOWLClassBean(concept, reasoner));
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

	private OWLClassBean createOWLClassBean(OWLClass concept, OWLReasoner reasoner) throws OWLReasonerException {
		OWLClassBean classBean = new OWLClassBean();
		classBean.setUri(concept.getURI().toString());
		String label = getLabel(concept);
		classBean.setLabel(label == null ? RenderingHelper.getLabelFromUri(concept.getURI()) : label);
		classBean.setUnsatisfiable(!reasoner.isSatisfiable(concept));
		return classBean;
	}

	private ClientSession getClientSession() {
		HttpServletRequest request = getThreadLocalRequest();
		return SessionHelper.getClientSession(request);
	}

	private String getLabel(OWLEntity entity, OWLOntology ontology) {
		LabelExtractor extractor = new LabelExtractor();
		for (OWLAnnotation<OWLObject> annotation : entity.getAnnotations(ontology)) {
			annotation.accept(extractor);
		}
		return extractor.getResult();
	}

	private String getLabel(OWLEntity entity) {
		for (OWLOntology ontology : SessionHelper.getClientSession(getThreadLocalRequest()).getOntologies()) {
			String label = getLabel(entity, ontology);
			if (label != null) {
				return label;
			}
		}
		return null;
	}

}

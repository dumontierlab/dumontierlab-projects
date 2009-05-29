package com.dumontierlab.jxta.owl.diag;

import java.net.URI;
import java.util.Collections;

import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.owlapi.PelletLoader;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

public class PelletConsistency {

	public static void main(String[] args) throws OWLOntologyCreationException {
		KnowledgeBase kb = new KnowledgeBase();
		PelletLoader loader = new PelletLoader(kb);

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		loader.setManager(manager);
		loader.load(Collections.singleton(manager.loadOntologyFromPhysicalURI(URI
				.create("http://protege.stanford.edu/plugins/owl/owl-library/koala.owl"))));
		System.out.println(kb.isConsistent());

		// System.out.println(kb.isSatisfiable(ATermUtils.makeTermAppl("http://semanticscience.org/inconsistentTBox#C")));

	}

}

package com.dumontierlab.jxta.owl.diag;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.jxta.owl.dht.DistributedHashTable;
import com.dumontierlab.jxta.owl.dht.WorkerPeer;
import com.dumontierlab.jxta.owl.discovery.Discovery;
import com.dumontierlab.jxta.owl.discovery.impl.DiscoveryImpl;
import com.dumontierlab.jxta.owl.jxta.JxtaService;
import com.dumontierlab.jxta.owl.jxta.JxtaServiceImpl;
import com.dumontierlab.jxta.owl.loader.Loader;
import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBase;
import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;

public class ClientTest {

	private static final Logger LOG = Logger.getLogger(ClientTest.class);

	// TODO: this is just to test the connection
	public static void main(String[] args) {
		Set<URI> seeds = Collections.singleton(URI.create("http://dsg.ce.unipr.it/research/SP2A/rdvlist.txt"));
		try {
			JxtaService jxta = new JxtaServiceImpl("testPeer-client", seeds, ".jxta");
			Discovery discovery = new DiscoveryImpl(jxta);
			Collection<WorkerPeer<DistributedKnowledgeBaseFragment>> peers = discovery.discoverPeers(2);
			DistributedKnowledgeBase kb = new DistributedKnowledgeBase(
					new DistributedHashTable<DistributedKnowledgeBaseFragment>(peers));

			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = manager.loadOntology(URI.create("http://www.w3.org/TR/owl-guide/wine.rdf"));
			Loader loader = new Loader(kb);
			loader.load(ontology, manager);

			kb.isConsistent();

		} catch (Exception e) {
			LOG.fatal(e.getMessage(), e);
			System.exit(1);
		}
	}
}

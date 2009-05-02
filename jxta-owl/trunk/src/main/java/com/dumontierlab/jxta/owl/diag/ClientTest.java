package com.dumontierlab.jxta.owl.diag;

import java.net.URI;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.mindswap.pellet.utils.ATermUtils;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.jxta.owl.Bootstrapper;
import com.dumontierlab.jxta.owl.dht.DistributedHashTable;
import com.dumontierlab.jxta.owl.dht.RemoteService;
import com.dumontierlab.jxta.owl.discovery.Discovery;
import com.dumontierlab.jxta.owl.inject.ServiceLocator;
import com.dumontierlab.jxta.owl.loader.Loader;
import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBase;
import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;

public class ClientTest {

	private static final Logger LOG = Logger.getLogger(ClientTest.class);

	// TODO: this is just to test the connection
	public static void main(String[] args) {
		try {
			// System.setProperty(JxtaOwlOptions.PEER_NAME_OPT, "client-test");
			ServiceLocator locator = Bootstrapper.bootstap();

			Discovery discovery = locator.getDiscovery();
			Collection<RemoteService<DistributedKnowledgeBaseFragment>> peers = discovery.discoverPeers(2);
			DistributedKnowledgeBase kb = new DistributedKnowledgeBase(
					new DistributedHashTable<DistributedKnowledgeBaseFragment>(peers));

			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = manager.loadOntologyFromPhysicalURI(URI
					.create("http://protege.stanford.edu/plugins/owl/owl-library/koala.owl"));
			Loader loader = new Loader(kb);
			// loader.load(Collections.singleton(manager.loadOntologyFromPhysicalURI(URI
			// .create("file:///Users/alex/ontologies/inconsistentTBox/inconsistentTBox"))),
			// manager);

			loader.load(ontology, manager);

			System.out
					.println("is DryEucalyptForest satisfiable? : "
							+ kb
									.isSatisfiable(ATermUtils
											.makeTermAppl("http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#DryEucalyptForest")));
			System.exit(0);

			boolean isSubclass = kb
					.isSubClassOf(
							ATermUtils
									.makeTermAppl("http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#MaleStudentWith3Daughters"),
							ATermUtils
									.makeTermAppl("http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#Person"));

			System.out.println("is subclass? " + isSubclass);

		} catch (Exception e) {
			LOG.fatal(e.getMessage(), e);
			System.exit(1);
		}
	}
}

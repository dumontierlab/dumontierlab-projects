/**
 * Copyright (c) 2009 Dumontierlab
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.dumontierlab.jxta.owl.reasoner;

import java.net.URI;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mindswap.pellet.utils.ATermUtils;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;

import com.dumontierlab.jxta.owl.dht.DistributedHashTable;
import com.dumontierlab.jxta.owl.dht.RemoteService;
import com.dumontierlab.jxta.owl.loader.Loader;
import com.dumontierlab.jxta.owl.reasoner.impl.DistributedKnowledgeBaseFragmentImpl;

/**
 * @author Alexander De Leon
 */
public class DistributedKnowledgeBaseTest {

	private static final int NUMBER_OF_VIRTUAL_PEERS = 6;

	private DistributedHashTable<DistributedKnowledgeBaseFragment> dht;
	private DistributedKnowledgeBase kb;

	@Before
	public void setup() {
		dht = new DistributedHashTable<DistributedKnowledgeBaseFragment>();
		for (int i = 0; i < NUMBER_OF_VIRTUAL_PEERS; i++) {
			dht.addPeer(new RemoteService<DistributedKnowledgeBaseFragment>("peer_" + i,
					new DistributedKnowledgeBaseFragmentImpl(dht)));
		}
		kb = new DistributedKnowledgeBase(dht);
	}

	@Test
	public void testIsSubClassOf() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromPhysicalURI(URI
				.create("http://semanticscience.googlecode.com/svn/trunk/ontology/saenger-rna_no_role_chains.owl"));
		Loader loader = new Loader(kb);
		loader.load(ontology, manager);

		boolean isSubclass = kb.isSubClassOf(ATermUtils
				.makeTermAppl("http://www.co-ode.org/ontologies/pizza/pizza_20041007.owl#TobascoPepperSauce"),
				ATermUtils.makeTermAppl("http://www.co-ode.org/ontologies/pizza/pizza_20041007.owl#PizzaTopping"));

		Assert.assertTrue(isSubclass);
	}
}

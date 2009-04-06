package com.dumontierlab.jxta.owl.reasoner;

import java.math.BigInteger;
import java.util.Set;
import java.util.logging.Logger;

import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.exceptions.UnsupportedFeatureException;
import org.mindswap.pellet.utils.ATermUtils;

import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;

import com.dumontierlab.jxta.owl.dht.DhtHelper;
import com.dumontierlab.jxta.owl.dht.WorkerPeer;

public class DistributedKnowledgeBase extends KnowledgeBase {

	private static final Logger LOG = Logger.getLogger(DistributedKnowledgeBase.class.getName());

	private WorkerPeer<DistributedKnowledgeBaseFragment>[] peers;
	private BigInteger[] peersHashes;

	@Override
	public void addClass(ATermAppl c) {
		if (c.equals(ATermUtils.TOP) || ATermUtils.isComplexClass(c)) {
			return;
		}
		WorkerPeer<DistributedKnowledgeBaseFragment> peer = getResponsiblePeer(c);
		peer.getService().addClass(c);
	}

	@Override
	public void addAllDifferent(ATermList list) {
		for (int i = 0; i < list.getLength(); i++) {
			ATermAppl indvidual1 = (ATermAppl) list.elementAt(i);
			for (int j = i + 1; j < list.getLength(); j++) {
				ATermAppl indvidual2 = (ATermAppl) list.elementAt(j);
				addDifferent(indvidual1, indvidual2);
			}
		}
	}

	@Override
	public boolean addAnnotation(ATermAppl s, ATermAppl p, ATermAppl o) {
		// Ignore annotations
		return false;
	}

	@Override
	public boolean addAnnotationProperty(ATermAppl p) {
		// Ignore annotations
		return false;
	}

	@Override
	public void addAsymmetricProperty(ATermAppl p) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (WorkerPeer<DistributedKnowledgeBaseFragment> peer : peers) {
			peer.getService().addAsymmetricProperty(p);
		}
	}

	@Override
	public void addComplementClass(ATermAppl c1, ATermAppl c2) {
		ATermAppl primitiveClass = null;
		ATermAppl complement = null;
		// TODO maybe check for isPrimitiveOrNeation
		if (ATermUtils.isPrimitive(c1)) {
			primitiveClass = c1;
			complement = c2;
		} else if (ATermUtils.isPrimitive(c2)) {
			primitiveClass = c2;
			complement = c1;
		} else {
			throw new UnsupportedFeatureException("General class axioms are not supported.");
		}
		ATermAppl notComplement = ATermUtils.makeNot(complement);

		if (primitiveClass.equals(notComplement)) {
			return;
		}

		addEquivalentClass(primitiveClass, notComplement);

	}

	@Override
	public boolean addPropertyValue(ATermAppl p, ATermAppl s, ATermAppl o) {
		WorkerPeer<DistributedKnowledgeBaseFragment> peer = getResponsiblePeer(s);
		return peer.getService().addPropertyValue(p, s, o);
	}

	@Override
	public void addDatatype(ATerm p) {

		super.addDatatype(p);
	}

	@Override
	public boolean addDatatypeProperty(ATerm p) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (WorkerPeer<DistributedKnowledgeBaseFragment> peer : peers) {
			if (!peer.getService().addDatatypeProperty(p)) {
				return false;
			}
		}
		return true;
	};

	@Override
	public void addDifferent(ATermAppl i1, ATermAppl i2) {
		WorkerPeer<DistributedKnowledgeBaseFragment> peer = getResponsiblePeer(i1);
		peer.getService().addDifferent(i1, i2);
	}

	@Override
	public void addDisjointClass(ATermAppl c1, ATermAppl c2) {
		WorkerPeer<DistributedKnowledgeBaseFragment> peer = getResponsiblePeer(c1);
		peer.getService().addDisjointClass(c1, c2);
	}

	@Override
	public void addDisjointClasses(ATermList classes) {
		Set<ATermAppl> primitives = ATermUtils.getPrimitives(classes);
		for (ATermAppl c1 : primitives) {
			for (ATermAppl c2 : primitives) {
				if (c1 != c2) {
					addDisjointClass(c1, c2);
				}
			}
		}
	}

	@Override
	public void addDomain(ATerm p, ATermAppl c) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (WorkerPeer<DistributedKnowledgeBaseFragment> peer : peers) {
			peer.getService().addDomain(p, c);
		}
	}

	@Override
	public void addEquivalentClass(ATermAppl c1, ATermAppl c2) {
		WorkerPeer<DistributedKnowledgeBaseFragment> peer = getResponsiblePeer(c1);
		peer.getService().addEquivalentClass(c1, c2);
	}

	@Override
	public void addRange(ATerm p, ATermAppl c) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (WorkerPeer<DistributedKnowledgeBaseFragment> peer : peers) {
			peer.getService().addRange(p, c);
		}
	}

	/* ------------- helper method --- */
	private WorkerPeer<DistributedKnowledgeBaseFragment> getResponsiblePeer(ATerm term) {
		BigInteger hash = DhtHelper.hash(term);

		int closest = DhtHelper.getClosest(hash, getPeersHashes());
		return peers[closest];
	}

	private BigInteger[] getPeersHashes() {
		if (peersHashes == null) {
			peersHashes = new BigInteger[peers.length];
			int i = 0;
			for (WorkerPeer<?> peer : peers) {
				peersHashes[i++] = DhtHelper.hash(peer.getPeerId(), true);
			}
		}
		return peersHashes;
	}

}

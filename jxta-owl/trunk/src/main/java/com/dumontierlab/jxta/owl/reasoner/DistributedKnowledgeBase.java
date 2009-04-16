package com.dumontierlab.jxta.owl.reasoner;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mindswap.pellet.Individual;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.exceptions.UnsupportedFeatureException;
import org.mindswap.pellet.utils.ATermUtils;

import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;

import com.dumontierlab.jxta.owl.dht.DhtHelper;
import com.dumontierlab.jxta.owl.dht.WorkerPeer;

public class DistributedKnowledgeBase extends KnowledgeBase {

	private static final Logger LOG = Logger.getLogger(DistributedKnowledgeBase.class);

	private final List<WorkerPeer<DistributedKnowledgeBaseFragment>> peers;
	private BigInteger[] peersHashes;

	public DistributedKnowledgeBase(Collection<WorkerPeer<DistributedKnowledgeBaseFragment>> peers) {
		this.peers = new ArrayList<WorkerPeer<DistributedKnowledgeBaseFragment>>(peers);
	}

	@Override
	public void addClass(ATermAppl c) {
		if (c.equals(ATermUtils.TOP) || ATermUtils.isComplexClass(c)) {
			return;
		}
		WorkerPeer<DistributedKnowledgeBaseFragment> peer = getResponsiblePeer(c);
		peer.getService().addClass(c);
	}

	@Override
	public Individual addIndividual(ATermAppl i) {
		WorkerPeer<DistributedKnowledgeBaseFragment> peer = getResponsiblePeer(i);
		peer.getService().addIndividual(i);
		return null; // TODO: Why does this method have to return something?
	}

	@Override
	public void addType(ATermAppl i, ATermAppl c) {
		WorkerPeer<DistributedKnowledgeBaseFragment> peer = getResponsiblePeer(i);
		peer.getService().addType(i, c);
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
			try {
				peer.getService().addAsymmetricProperty(p);
			} catch (RemoteException e) {
				// TODO: need to wrap exception inside a runtime since the
				// method signature cannot change.
				throw new RuntimeException(e);
			}
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
		peer.getService().addPropertyValue(p, s, o);
		return true;
	}

	@Override
	public void addDatatype(ATerm p) {

		super.addDatatype(p);
	}

	@Override
	public boolean addObjectProperty(ATerm p) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (WorkerPeer<DistributedKnowledgeBaseFragment> peer : peers) {
			peer.getService().addObjectProperty((ATermAppl) p);
		}
		return true;
	}

	@Override
	public boolean addDatatypeProperty(ATerm p) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (WorkerPeer<DistributedKnowledgeBaseFragment> peer : peers) {
			peer.getService().addDatatypeProperty((ATermAppl) p);
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
			peer.getService().addDomain((ATermAppl) p, c);
		}
	}

	@Override
	public void addEquivalentClass(ATermAppl c1, ATermAppl c2) {
		WorkerPeer<DistributedKnowledgeBaseFragment> peer = getResponsiblePeer(c1);
		peer.getService().addEquivalentClass(c1, c2);
	}

	@Override
	public void addEquivalentProperty(ATermAppl p1, ATermAppl p2) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (WorkerPeer<DistributedKnowledgeBaseFragment> peer : peers) {
			peer.getService().addEquivalentProperty(p1, p2);
		}
	}

	@Override
	public void addRange(ATerm p, ATermAppl c) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (WorkerPeer<DistributedKnowledgeBaseFragment> peer : peers) {
			peer.getService().addRange((ATermAppl) p, c);
		}
	}

	@Override
	public void addTransitiveProperty(ATermAppl p) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (WorkerPeer<DistributedKnowledgeBaseFragment> peer : peers) {
			peer.getService().addTransitiveProperty(p);
		}
	}

	@Override
	public void addSame(ATermAppl i1, ATermAppl i2) {
		WorkerPeer<DistributedKnowledgeBaseFragment> peer = getResponsiblePeer(i1);
		peer.getService().addSame(i1, i2);
	}

	@Override
	public boolean isConsistent() {
		for (WorkerPeer<DistributedKnowledgeBaseFragment> peer : peers) {
			boolean isConsistent = peer.getService().isConsistent();
			if (!isConsistent) {
				return false;
			}
		}
		return true;
	}

	/* ------------- helper method --- */
	private WorkerPeer<DistributedKnowledgeBaseFragment> getResponsiblePeer(ATerm term) {
		BigInteger hash = DhtHelper.hash(term);

		int closest = DhtHelper.getClosest(hash, getPeersHashes());
		return peers.get(closest);
	}

	private BigInteger[] getPeersHashes() {
		if (peersHashes == null) {
			peersHashes = new BigInteger[peers.size()];
			int i = 0;
			for (WorkerPeer<?> peer : peers) {
				peersHashes[i++] = DhtHelper.hash(peer.getPeerId(), true);
			}
		}
		return peersHashes;
	}

}

package com.dumontierlab.jxta.owl.reasoner;

import java.rmi.RemoteException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mindswap.pellet.Individual;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.exceptions.UnsupportedFeatureException;
import org.mindswap.pellet.utils.ATermUtils;

import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;

import com.dumontierlab.jxta.owl.dht.DistributedHashTable;
import com.dumontierlab.jxta.owl.dht.RemoteService;

public class DistributedKnowledgeBase extends KnowledgeBase {

	private static final Logger LOG = Logger.getLogger(DistributedKnowledgeBase.class);

	private final DistributedHashTable<DistributedKnowledgeBaseFragment> hashTable;

	public DistributedKnowledgeBase(DistributedHashTable<DistributedKnowledgeBaseFragment> hashTable) {
		this.hashTable = hashTable;
	}

	@Override
	public void addClass(ATermAppl c) {
		if (c.equals(ATermUtils.TOP) || ATermUtils.isComplexClass(c)) {
			return;
		}
		RemoteService<DistributedKnowledgeBaseFragment> peer = hashTable.getResponsiblePeer(c);
		peer.getService().addClass(c);
	}

	@Override
	public Individual addIndividual(ATermAppl i) {
		RemoteService<DistributedKnowledgeBaseFragment> peer = hashTable.getResponsiblePeer(i);
		peer.getService().addIndividual(i);
		return null; // TODO: Why does this method have to return something?
	}

	@Override
	public void addType(ATermAppl i, ATermAppl c) {
		RemoteService<DistributedKnowledgeBaseFragment> peer = hashTable.getResponsiblePeer(i);
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
		for (RemoteService<DistributedKnowledgeBaseFragment> peer : hashTable.getPeers()) {
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
		ATermAppl[] terms = getInTerminologyOrder(c1, c2);

		ATermAppl notComplement = ATermUtils.makeNot(terms[1]);

		if (terms[0].equals(notComplement)) {
			return;
		}
		addEquivalentClass(terms[0], notComplement);

	}

	@Override
	public boolean addPropertyValue(ATermAppl p, ATermAppl s, ATermAppl o) {
		RemoteService<DistributedKnowledgeBaseFragment> peer = hashTable.getResponsiblePeer(s);
		peer.getService().addPropertyValue(p, s, o);
		return true;
	}

	@Override
	public void addDatatype(ATerm p) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (RemoteService<DistributedKnowledgeBaseFragment> peer : hashTable.getPeers()) {
			peer.getService().addDatatype(p);
		}
	}

	@Override
	public boolean addObjectProperty(ATerm p) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (RemoteService<DistributedKnowledgeBaseFragment> peer : hashTable.getPeers()) {
			peer.getService().addObjectProperty((ATermAppl) p);
		}
		return true;
	}

	@Override
	public boolean addDatatypeProperty(ATerm p) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (RemoteService<DistributedKnowledgeBaseFragment> peer : hashTable.getPeers()) {
			peer.getService().addDatatypeProperty((ATermAppl) p);
		}
		return true;
	};

	@Override
	public void addDifferent(ATermAppl i1, ATermAppl i2) {
		RemoteService<DistributedKnowledgeBaseFragment> peer = hashTable.getResponsiblePeer(i1);
		peer.getService().addDifferent(i1, i2);
	}

	@Override
	public void addDisjointClass(ATermAppl c1, ATermAppl c2) {
		ATermAppl terms[] = getInTerminologyOrder(c1, c2);
		RemoteService<DistributedKnowledgeBaseFragment> peer = hashTable.getResponsiblePeer(terms[0]);
		peer.getService().addDisjointClass(terms[0], terms[1]);
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
		for (RemoteService<DistributedKnowledgeBaseFragment> peer : hashTable.getPeers()) {
			peer.getService().addDomain((ATermAppl) p, c);
		}
	}

	@Override
	public void addEquivalentClass(ATermAppl c1, ATermAppl c2) {
		ATermAppl terms[] = getInTerminologyOrder(c1, c2);
		RemoteService<DistributedKnowledgeBaseFragment> peer = hashTable.getResponsiblePeer(terms[0]);
		peer.getService().addEquivalentClass(terms[0], terms[1]);
	}

	@Override
	public void addSubClass(ATermAppl sub, ATermAppl sup) {
		if (!ATermUtils.isPrimitive(sub)) {
			throw new UnsupportedFeatureException("General class axioms are not supported.");
		}
		RemoteService<DistributedKnowledgeBaseFragment> peer = hashTable.getResponsiblePeer(sub);
		peer.getService().addSubClass(sub, sup);
	}

	@Override
	public void addEquivalentProperty(ATermAppl p1, ATermAppl p2) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (RemoteService<DistributedKnowledgeBaseFragment> peer : hashTable.getPeers()) {
			peer.getService().addEquivalentProperty(p1, p2);
		}
	}

	@Override
	public void addRange(ATerm p, ATermAppl c) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (RemoteService<DistributedKnowledgeBaseFragment> peer : hashTable.getPeers()) {
			peer.getService().addRange((ATermAppl) p, c);
		}
	}

	@Override
	public void addTransitiveProperty(ATermAppl p) {
		// TODO: Properties are not distributed therefore axioms about them are
		// sent to every peer.
		for (RemoteService<DistributedKnowledgeBaseFragment> peer : hashTable.getPeers()) {
			peer.getService().addTransitiveProperty(p);
		}
	}

	@Override
	public void addSame(ATermAppl i1, ATermAppl i2) {
		RemoteService<DistributedKnowledgeBaseFragment> peer = hashTable.getResponsiblePeer(i1);
		peer.getService().addSame(i1, i2);
	}

	@Override
	public boolean isConsistent() {
		for (RemoteService<DistributedKnowledgeBaseFragment> peer : hashTable.getPeers()) {
			boolean isConsistent = peer.getService().isConsistent();
			if (!isConsistent) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isSatisfiable(ATermAppl c) {
		if (!ATermUtils.isPrimitive(c)) {
			// just pick a peer
			hashTable.getPeers().iterator().next().getService().isSatisfiable(c);
		}
		RemoteService<DistributedKnowledgeBaseFragment> peer = hashTable.getResponsiblePeer(c);
		return peer.getService().isSatisfiable(c);
	}

	private ATermAppl[] getInTerminologyOrder(ATermAppl c1, ATermAppl c2) {
		// TODO maybe check for isPrimitiveOrNeation
		ATermAppl[] terms = new ATermAppl[2];
		if (ATermUtils.isPrimitive(c1)) {
			terms[0] = c1;
			terms[1] = c2;
		} else if (ATermUtils.isPrimitive(c2)) {
			terms[0] = c2;
			terms[1] = c1;
		} else {
			throw new UnsupportedFeatureException("General class axioms are not supported.");
		}
		return terms;
	}

	@Override
	public boolean isSubClassOf(ATermAppl c1, ATermAppl c2) {
		ATermAppl test = ATermUtils.makeAnd(c1, ATermUtils.negate(c2));
		return !isSatisfiable(test);
	}
}

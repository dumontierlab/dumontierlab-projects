package com.dumontierlab.jxta.owl.reasoner.impl;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.utils.ATermUtils;
import org.mindswap.pellet.utils.Pair;

import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;

import com.dumontierlab.jxta.owl.dht.DistributedHashTable;
import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;

public class DistributedKnowledgeBaseFragmentImpl implements DistributedKnowledgeBaseFragment {

	private static final Logger LOG = Logger.getLogger(DistributedKnowledgeBaseFragmentImpl.class);

	private final DistributedHashTable<DistributedKnowledgeBaseFragment> hashTable;
	private final KnowledgeBase kb;
	private final DistributedTBoxFragment tbox;

	public DistributedKnowledgeBaseFragmentImpl() {
		this(new DistributedHashTable<DistributedKnowledgeBaseFragment>());
	}

	/**
	 * for testing
	 */
	public DistributedKnowledgeBaseFragmentImpl(DistributedHashTable<DistributedKnowledgeBaseFragment> dht) {
		LOG.debug("creating DistributedKnowledgeBaseFragment");
		// Initialize the hashTable.
		hashTable = dht;

		// PelletOptions.DEFAULT_COMPLETION_STRATEGY =
		// DistributedCompletionStrategy.class;
		kb = new KnowledgeBase();
		tbox = new DistributedTBoxFragment(kb, hashTable);
		kb.setTBox(tbox);
	}

	@Override
	public void addAsymmetricProperty(ATermAppl p) {
		LOG.debug("addAsymmetricProperty(" + p + ")");
		kb.addSymmetricProperty(p);
	}

	@Override
	public void addClass(ATermAppl c) {
		LOG.debug("addClass(" + c + ")");
		kb.addClass(c);
	}

	@Override
	public void addDatatypeProperty(ATermAppl p) {
		LOG.debug("addDatatypeProperty(" + p + ")");
		kb.addDatatypeProperty(p);
	}

	@Override
	public void addDifferent(ATermAppl i1, ATermAppl i2) {
		LOG.debug("addDifferent(" + i1 + ", " + i2 + ")");
		if (isRemoteIndividual(i2)) {
			addRemoteIndividual(i2);
		}
		kb.addDifferent(i1, i2);
	}

	@Override
	public void addDisjointClass(ATermAppl c1, ATermAppl c2) {
		LOG.debug("addDisjointClass(" + c1 + ", " + c2 + ")");
		addRemotePrimitives(c2);
		kb.addDisjointClass(c1, c2);
	}

	@Override
	public void addDomain(ATermAppl p, ATermAppl c) {
		LOG.debug("addDomain(" + p + ", " + c + ")");
		addRemotePrimitives(c);
		kb.addDomain(p, c);
	}

	@Override
	public void addEquivalentClass(ATermAppl c1, ATermAppl c2) {
		LOG.debug("addEquivalentClass(" + c1 + ", " + c2 + ")");
		addRemotePrimitives(c2);
		kb.addEquivalentClass(c1, c2);
	}

	@Override
	public void addPropertyValue(ATermAppl p, ATermAppl s, ATermAppl o) {
		LOG.debug("addPropertyValue(" + p + ", " + s + ", " + o + ")");
		if (!kb.isDatatypeProperty(p)) {
			if (isRemoteIndividual(o)) {
				addRemoteIndividual(o);
			}
		}
		kb.addPropertyValue(p, s, o);
	}

	@Override
	public void addRange(ATermAppl p, ATermAppl c) {
		LOG.debug("addRange(" + p + ", " + c + ")");
		if (!kb.isDatatypeProperty(p)) {
			addRemotePrimitives(c);
		}
		kb.addRange(p, c);
	}

	@Override
	public void addIndividual(ATermAppl i) {
		LOG.debug("addIndividual(" + i + ")");
		kb.addIndividual(i);
	}

	@Override
	public void addType(ATermAppl i, ATermAppl c) {
		LOG.debug("addType(" + i + ", " + c + ")");
		addRemotePrimitives(c);
		kb.addType(i, c);
	}

	@Override
	public void addObjectProperty(ATermAppl p) {
		LOG.debug("addObjectProperty(" + p + ")");
		kb.addObjectProperty(p);
	}

	@Override
	public void addEquivalentProperty(ATermAppl p1, ATermAppl p2) {
		LOG.debug("addEquivalentProperty(" + p1 + ", " + p2 + ")");
		kb.addEquivalentProperty(p1, p2);
	}

	@Override
	public void addTransitiveProperty(ATermAppl p) {
		LOG.debug("addTransitiveProperty(" + p + ")");
		kb.addTransitiveProperty(p);
	}

	@Override
	public void addSame(ATermAppl i1, ATermAppl i2) {
		LOG.debug("addSame(" + i1 + ",  " + i2 + ")");
		if (isRemoteIndividual(i2)) {
			addRemoteIndividual(i2);
		}
		kb.addSame(i1, i2);
	}

	@Override
	public void addSubClass(ATermAppl sub, ATermAppl sup) {
		LOG.debug("addSubClass(" + sub + ",  " + sup + ")");
		addRemotePrimitives(sup);
		kb.addSubClass(sub, sup);
	}

	@Override
	public void addDatatype(ATerm p) {
		LOG.debug("addDatatype(" + p + ")");
		kb.addDatatype(p);
	}

	@Override
	public boolean isConsistent() {
		LOG.debug("Checking consistency");
		return kb.isConsistent();
	}

	@Override
	public boolean isSatisfiable(ATermAppl c) {
		LOG.debug("Concept satisfiability for: " + c);
		return kb.isSatisfiable(c);
	}

	@Override
	public List<Pair<ATermAppl, Set<ATermAppl>>> unfold(ATermAppl c) {
		return kb.getTBox().unfold(c);
	}

	public DistributedHashTable<DistributedKnowledgeBaseFragment> getHashTable() {
		return hashTable;
	}

	private void addRemotePrimitives(ATermAppl description) {
		if (ATermUtils.isPrimitive(description)) {
			if (isRemoteClass(description)) {
				addRemoteClass(description);
			}
		} else if (ATermUtils.isNot(description)) {
			addRemotePrimitives((ATermAppl) description.getArgument(0));
		} else if (ATermUtils.isAnd(description)) {
			ATermList list = (ATermList) description.getArgument(0);
			for (int i = 0; i < list.getLength(); i++) {
				addRemotePrimitives((ATermAppl) list.elementAt(i));
			}
		} else if (ATermUtils.isSomeValues(description)) {
			addRemotePrimitives((ATermAppl) description.getArgument(1));
		} else if (ATermUtils.isCard(description)) {
			addRemotePrimitives((ATermAppl) description.getArgument(2));
		} else if (ATermUtils.isOr(description)) {
			ATermList list = (ATermList) description.getArgument(0);
			for (int i = 0; i < list.getLength(); i++) {
				addRemotePrimitives((ATermAppl) list.elementAt(i));
			}
		} else if (ATermUtils.isAllValues(description)) {
			addRemotePrimitives((ATermAppl) description.getArgument(1));
		} else if (ATermUtils.isNominal(description)) {
			if (!ATermUtils.isLiteral((ATermAppl) description.getArgument(0))) {
				ATermAppl individual = (ATermAppl) description.getArgument(0);
				if (isRemoteIndividual(individual)) {
					addRemoteIndividual(individual);
				}
			}
		}
	}

	private boolean isRemoteIndividual(ATermAppl individual) {
		return kb.getABox().getIndividual(individual) == null;
	}

	private boolean isRemoteClass(ATermAppl c) {
		return !ATermUtils.isTop(c) && !ATermUtils.isBottom(c) && !kb.getClasses().contains(c);
	}

	private void addRemoteIndividual(ATermAppl i) {
		addIndividual(i);
		kb.addType(i, DistributedReasoningHelper.getRemoteClass());
	}

	private void addRemoteClass(ATermAppl c) {
		tbox.addRemoteClass(c);
	}
}

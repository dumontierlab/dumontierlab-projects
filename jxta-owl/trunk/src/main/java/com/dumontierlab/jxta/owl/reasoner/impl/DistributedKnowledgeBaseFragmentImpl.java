package com.dumontierlab.jxta.owl.reasoner.impl;

import org.apache.log4j.Logger;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.PelletOptions;

import aterm.ATermAppl;

import com.dumontierlab.jxta.owl.reasoner.DistributedCompletionStrategy;
import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;

public class DistributedKnowledgeBaseFragmentImpl implements DistributedKnowledgeBaseFragment {

	private static final Logger LOG = Logger.getLogger(DistributedKnowledgeBaseFragmentImpl.class);

	private final KnowledgeBase kb;

	public DistributedKnowledgeBaseFragmentImpl() {
		LOG.debug("creating DistributedKnowledgeBaseFragment");
		PelletOptions.DEFAULT_COMPLETION_STRATEGY = DistributedCompletionStrategy.class;
		kb = new KnowledgeBase();
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
		if (kb.getABox().getIndividual(i2) == null) {
			addRemoteIndividual(i2);
		}
		kb.addDifferent(i1, i2);
	}

	@Override
	public void addDisjointClass(ATermAppl c1, ATermAppl c2) {
		LOG.debug("addDisjointClass(" + c1 + ", " + c2 + ")");
		kb.addDisjointClass(c1, c2);
	}

	@Override
	public void addDomain(ATermAppl p, ATermAppl c) {
		LOG.debug("addDomain(" + p + ", " + c + ")");
		kb.addDomain(p, c);
	}

	@Override
	public void addEquivalentClass(ATermAppl c1, ATermAppl c2) {
		LOG.debug("addEquivalentClass(" + c1 + ", " + c2 + ")");
		kb.addEquivalentClass(c1, c2);
	}

	@Override
	public void addPropertyValue(ATermAppl p, ATermAppl s, ATermAppl o) {
		LOG.debug("addPropertyValue(" + p + ", " + s + ", " + o + ")");
		kb.addPropertyValue(p, s, o);
	}

	@Override
	public void addRange(ATermAppl p, ATermAppl c) {
		LOG.debug("addRange(" + p + ", " + c + ")");
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
		kb.addSame(i1, i2);
	}

	@Override
	public boolean isConsistent() {
		return kb.isConsistent();
	}

	private void addRemoteIndividual(ATermAppl i) {
		addIndividual(i);
		addType(i, DistributedReasoningHelper.getRemoteClass());
	}
}

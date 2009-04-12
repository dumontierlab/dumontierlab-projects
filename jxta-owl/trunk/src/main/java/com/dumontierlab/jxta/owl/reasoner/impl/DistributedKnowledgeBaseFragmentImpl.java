package com.dumontierlab.jxta.owl.reasoner.impl;

import org.mindswap.pellet.KnowledgeBase;

import aterm.ATerm;
import aterm.ATermAppl;

import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;

public class DistributedKnowledgeBaseFragmentImpl implements DistributedKnowledgeBaseFragment {

	private final KnowledgeBase kb;

	public DistributedKnowledgeBaseFragmentImpl() {
		kb = new KnowledgeBase();
	}

	@Override
	public void addAsymmetricProperty(ATermAppl p) {
		kb.addSymmetricProperty(p);
	}

	@Override
	public void addClass(ATermAppl c) {
		kb.addClass(c);
	}

	@Override
	public void addDatatypeProperty(ATerm p) {
		kb.addDatatypeProperty(p);
	}

	@Override
	public void addDifferent(ATermAppl i1, ATermAppl i2) {
		kb.addDifferent(i1, i2);
	}

	@Override
	public void addDisjointClass(ATermAppl c1, ATermAppl c2) {
		kb.addDisjointClass(c1, c2);
	}

	@Override
	public void addDomain(ATerm p, ATermAppl c) {
		kb.addDomain(p, c);
	}

	@Override
	public void addEquivalentClass(ATermAppl c1, ATermAppl c2) {
		kb.addEquivalentClass(c1, c2);
	}

	@Override
	public void addPropertyValue(ATermAppl p, ATermAppl s, ATermAppl o) {
		kb.addPropertyValue(p, s, o);
	}

	@Override
	public void addRange(ATerm p, ATermAppl c) {
		kb.addRange(p, c);
	}

}

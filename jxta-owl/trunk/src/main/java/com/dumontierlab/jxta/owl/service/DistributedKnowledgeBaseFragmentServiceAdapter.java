package com.dumontierlab.jxta.owl.service;

import java.rmi.RemoteException;

import aterm.ATerm;
import aterm.ATermAppl;

import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;

public class DistributedKnowledgeBaseFragmentServiceAdapter implements DistributedKnowledgeBaseFragment {

	private final DistributedKnowledgeBaseFragmentService service;

	public DistributedKnowledgeBaseFragmentServiceAdapter(DistributedKnowledgeBaseFragmentService service) {
		this.service = service;
	}

	@Override
	public void addAsymmetricProperty(ATermAppl p) throws RemoteException {
		service.addAsymmetricProperty(p.toString());
	}

	@Override
	public void addClass(ATermAppl c) {
		service.addClass(c.toString());
	}

	@Override
	public void addDatatypeProperty(ATerm p) {
		service.addDatatypeProperty(p.toString());
	}

	@Override
	public void addDifferent(ATermAppl i1, ATermAppl i2) {
		service.addDifferent(i1.toString(), i2.toString());
	}

	@Override
	public void addDisjointClass(ATermAppl c1, ATermAppl c2) {
		service.addDisjointClass(c1.toString(), c2.toString());
	}

	@Override
	public void addDomain(ATerm p, ATermAppl c) {
		service.addDomain(p.toString(), c.toString());
	}

	@Override
	public void addEquivalentClass(ATermAppl c1, ATermAppl c2) {
		service.addEquivalentClass(c1.toString(), c2.toString());
	}

	@Override
	public void addPropertyValue(ATermAppl p, ATermAppl s, ATermAppl o) {
		service.addPropertyValue(p.toString(), s.toString(), o.toString());
	}

	@Override
	public void addRange(ATerm p, ATermAppl c) {
		service.addRange(p.toString(), c.toString());
	}

}

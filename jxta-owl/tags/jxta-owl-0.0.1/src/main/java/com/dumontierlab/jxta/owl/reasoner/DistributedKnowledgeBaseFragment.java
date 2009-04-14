package com.dumontierlab.jxta.owl.reasoner;

import java.rmi.RemoteException;

import aterm.ATermAppl;

public interface DistributedKnowledgeBaseFragment {

	void addClass(ATermAppl c);

	void addAsymmetricProperty(ATermAppl p) throws RemoteException;

	void addPropertyValue(ATermAppl p, ATermAppl s, ATermAppl o);

	void addDatatypeProperty(ATermAppl p);

	void addDifferent(ATermAppl i1, ATermAppl i2);

	void addDisjointClass(ATermAppl c1, ATermAppl c2);

	void addDomain(ATermAppl p, ATermAppl c);

	void addEquivalentClass(ATermAppl c1, ATermAppl c2);

	void addRange(ATermAppl p, ATermAppl c);

	void addIndividual(ATermAppl i);

	void addType(ATermAppl i, ATermAppl c);

	void addObjectProperty(ATermAppl p);

	void addEquivalentProperty(ATermAppl p1, ATermAppl p2);

	void addTransitiveProperty(ATermAppl p);

	void addSame(ATermAppl i1, ATermAppl i2);

}

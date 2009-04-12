package com.dumontierlab.jxta.owl.reasoner;

import java.rmi.RemoteException;

import aterm.ATerm;
import aterm.ATermAppl;

public interface DistributedKnowledgeBaseFragment {

	void addClass(ATermAppl c);

	void addAsymmetricProperty(ATermAppl p) throws RemoteException;

	void addPropertyValue(ATermAppl p, ATermAppl s, ATermAppl o);

	void addDatatypeProperty(ATerm p);

	void addDifferent(ATermAppl i1, ATermAppl i2);

	void addDisjointClass(ATermAppl c1, ATermAppl c2);

	void addDomain(ATerm p, ATermAppl c);

	void addEquivalentClass(ATermAppl c1, ATermAppl c2);

	void addRange(ATerm p, ATermAppl c);

}

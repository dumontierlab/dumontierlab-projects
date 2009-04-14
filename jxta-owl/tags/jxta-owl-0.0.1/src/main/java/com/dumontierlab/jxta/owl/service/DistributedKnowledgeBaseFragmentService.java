package com.dumontierlab.jxta.owl.service;

import java.rmi.RemoteException;

public interface DistributedKnowledgeBaseFragmentService {

	void addClass(String c);

	void addAsymmetricProperty(String p) throws RemoteException;

	void addPropertyValue(String p, String s, String o);

	void addDatatypeProperty(String p);

	void addDifferent(String i1, String i2);

	void addDisjointClass(String c1, String c2);

	void addDomain(String p, String c);

	void addEquivalentClass(String c1, String c2);

	void addRange(String p, String c);

	void addIndividual(String string);

	void addType(String i, String c);

	void addObjectProperty(String p);

	void addEquivalentProperty(String p1, String p2);

	void addTransitiveProperty(String p);

	void addSame(String i1, String i2);
}
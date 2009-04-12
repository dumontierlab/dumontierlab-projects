package com.dumontierlab.jxta.owl.service;

import java.rmi.RemoteException;

import net.jxta.soap.ServiceDescriptor;

import org.mindswap.pellet.utils.ATermUtils;

import aterm.ATermAppl;
import aterm.ATermFactory;

import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;

public class DistributedKnowledgeBaseFragmentServiceImpl implements DistributedKnowledgeBaseFragmentService {

	public static final ServiceDescriptor DESCRIPTOR = new ServiceDescriptor(
			DistributedKnowledgeBaseFragmentServiceImpl.class.getName(), // class
			DistributedKnowledgeBaseFragmentServiceImpl.class.getName(), // name
			"0.1", // version
			"Dumontierlab", // creator
			"jxta:/dumontierlab.com/jxta-owl/service/" + DistributedKnowledgeBaseFragmentServiceImpl.class.getName(), // specURI
			"Distributed Knowledge Base Service", // description
			"urn:jxta:jxta-NetGroup", // peergroup ID
			"JXTA NetPeerGroup", // PeerGroup name
			"JXTA NetPeerGroup", // PeerGroup description
			false, // secure policy flag (use default=false)
			null); // security policy type (use no policy)

	private DistributedKnowledgeBaseFragment fragment;
	private final ATermFactory factory;

	public DistributedKnowledgeBaseFragmentServiceImpl() {
		factory = ATermUtils.getFactory();

	}

	public void addClass(String c) {
		fragment.addClass((ATermAppl) factory.parse(c));
	}

	public void addAsymmetricProperty(String p) throws RemoteException {
		fragment.addAsymmetricProperty((ATermAppl) factory.parse(p));
	}

	public void addPropertyValue(String p, String s, String o) {
		fragment.addPropertyValue((ATermAppl) factory.parse(p), (ATermAppl) factory.parse(s), (ATermAppl) factory
				.parse(o));
	}

	public void addDatatypeProperty(String p) {
		fragment.addDatatypeProperty(factory.parse(p));
	}

	public void addDifferent(String i1, String i2) {
		fragment.addDifferent((ATermAppl) factory.parse(i1), (ATermAppl) factory.parse(i2));
	}

	public void addDisjointClass(String c1, String c2) {
		fragment.addDisjointClass((ATermAppl) factory.parse(c1), (ATermAppl) factory.parse(c2));
	}

	public void addDomain(String p, String c) {
		fragment.addDomain(factory.parse(p), (ATermAppl) factory.parse(c));
	}

	public void addEquivalentClass(String c1, String c2) {
		fragment.addEquivalentClass((ATermAppl) factory.parse(c1), (ATermAppl) factory.parse(c2));
	}

	public void addRange(String p, String c) {
		fragment.addEquivalentClass((ATermAppl) factory.parse(p), (ATermAppl) factory.parse(c));
	}
}

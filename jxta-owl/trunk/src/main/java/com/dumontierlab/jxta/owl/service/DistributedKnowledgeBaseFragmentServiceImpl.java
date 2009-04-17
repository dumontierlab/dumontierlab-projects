package com.dumontierlab.jxta.owl.service;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.jxta.soap.ServiceDescriptor;

import org.apache.log4j.Logger;
import org.mindswap.pellet.utils.Pair;

import aterm.ATermAppl;

import com.dumontierlab.jxta.owl.io.ATermSerializer;
import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;
import com.dumontierlab.jxta.owl.reasoner.impl.DistributedKnowledgeBaseFragmentImpl;

public class DistributedKnowledgeBaseFragmentServiceImpl implements DistributedKnowledgeBaseFragmentService {

	private static final Logger LOG = Logger.getLogger(DistributedKnowledgeBaseFragmentServiceImpl.class);

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

	private final DistributedKnowledgeBaseFragment fragment;

	public DistributedKnowledgeBaseFragmentServiceImpl() {
		fragment = SingletonKbFragment.getInstance();
	}

	public void addClass(String c) {
		fragment.addClass(deserialize(c));
	}

	public void addAsymmetricProperty(String p) throws RemoteException {
		fragment.addAsymmetricProperty(deserialize(p));
	}

	public void addPropertyValue(String p, String s, String o) {
		fragment.addPropertyValue(deserialize(p), deserialize(s), deserialize(o));
	}

	public void addDatatypeProperty(String p) {
		fragment.addDatatypeProperty(deserialize(p));
	}

	public void addDifferent(String i1, String i2) {
		fragment.addDifferent(deserialize(i1), deserialize(i2));
	}

	public void addDisjointClass(String c1, String c2) {
		fragment.addDisjointClass(deserialize(c1), deserialize(c2));
	}

	public void addDomain(String p, String c) {
		fragment.addDomain(deserialize(p), deserialize(c));
	}

	public void addEquivalentClass(String c1, String c2) {
		fragment.addEquivalentClass(deserialize(c1), deserialize(c2));
	}

	public void addRange(String p, String c) {
		fragment.addEquivalentClass(deserialize(p), deserialize(c));
	}

	@Override
	public void addIndividual(String i) {
		fragment.addIndividual(deserialize(i));
	}

	@Override
	public void addType(String i, String c) {
		fragment.addType(deserialize(i), deserialize(c));
	}

	@Override
	public void addObjectProperty(String p) {
		fragment.addObjectProperty(deserialize(p));
	}

	@Override
	public void addEquivalentProperty(String p1, String p2) {
		fragment.addEquivalentProperty(deserialize(p1), deserialize(p2));
	}

	@Override
	public void addTransitiveProperty(String p) {
		fragment.addTransitiveProperty(deserialize(p));
	}

	@Override
	public void addSame(String i1, String i2) {
		fragment.addSame(deserialize(i1), deserialize(i2));
	}

	@Override
	public boolean isConsistent() {
		return fragment.isConsistent();
	}

	@Override
	public HashMap<String, String[]> unfold(String c) {
		List<Pair<ATermAppl, Set<ATermAppl>>> unfolding = fragment.unfold(deserialize(c));
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		for (Pair<ATermAppl, Set<ATermAppl>> pair : unfolding) {
			String[] array = new String[pair.second.size()];
			int index = 0;
			for (ATermAppl term : pair.second) {
				array[index++] = serialize(term);
			}
			map.put(serialize(pair.first), array);
		}
		return map;
	}

	private String serialize(ATermAppl term) {
		return ATermSerializer.serialize(term);
	}

	private ATermAppl deserialize(String string) {
		try {
			return ATermSerializer.deserialize(string);
		} catch (IOException e) {
			LOG.error("Aterm parsing exception: " + string, e);
			return null;
		}
	}

	private static class SingletonKbFragment {
		private static DistributedKnowledgeBaseFragment instance;

		public static DistributedKnowledgeBaseFragment getInstance() {
			if (instance == null) {
				instance = new DistributedKnowledgeBaseFragmentImpl();
			}
			return instance;
		}

	}
}

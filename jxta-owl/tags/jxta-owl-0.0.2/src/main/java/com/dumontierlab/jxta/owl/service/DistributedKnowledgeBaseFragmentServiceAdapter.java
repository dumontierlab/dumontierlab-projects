package com.dumontierlab.jxta.owl.service;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.mindswap.pellet.utils.Pair;

import aterm.ATerm;
import aterm.ATermAppl;

import com.dumontierlab.jxta.owl.io.ATermSerializer;
import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;

public class DistributedKnowledgeBaseFragmentServiceAdapter implements DistributedKnowledgeBaseFragment {

	private static final Logger LOG = Logger.getLogger(DistributedKnowledgeBaseFragmentServiceAdapter.class);

	private final DistributedKnowledgeBaseFragmentService service;

	public DistributedKnowledgeBaseFragmentServiceAdapter(DistributedKnowledgeBaseFragmentService service) {
		this.service = service;
	}

	@Override
	public void addAsymmetricProperty(ATermAppl p) throws RemoteException {
		service.addAsymmetricProperty(serialize(p));
	}

	@Override
	public void addClass(ATermAppl c) {
		service.addClass(serialize(c));
	}

	@Override
	public void addDatatypeProperty(ATermAppl p) {
		service.addDatatypeProperty(serialize(p));
	}

	@Override
	public void addDifferent(ATermAppl i1, ATermAppl i2) {
		service.addDifferent(serialize(i1), serialize(i2));
	}

	@Override
	public void addDisjointClass(ATermAppl c1, ATermAppl c2) {
		service.addDisjointClass(serialize(c1), serialize(c2));
	}

	@Override
	public void addDomain(ATermAppl p, ATermAppl c) {
		service.addDomain(serialize(p), serialize(c));
	}

	@Override
	public void addEquivalentClass(ATermAppl c1, ATermAppl c2) {
		service.addEquivalentClass(serialize(c1), serialize(c2));
	}

	@Override
	public void addPropertyValue(ATermAppl p, ATermAppl s, ATermAppl o) {
		service.addPropertyValue(serialize(p), serialize(s), serialize(o));
	}

	@Override
	public void addRange(ATermAppl p, ATermAppl c) {
		service.addRange(serialize(p), serialize(c));
	}

	@Override
	public void addIndividual(ATermAppl i) {
		service.addIndividual(serialize(i));
	}

	@Override
	public void addType(ATermAppl i, ATermAppl c) {
		service.addType(serialize(i), serialize(c));
	}

	@Override
	public void addObjectProperty(ATermAppl p) {
		service.addObjectProperty(serialize(p));
	}

	@Override
	public void addEquivalentProperty(ATermAppl p1, ATermAppl p2) {
		service.addEquivalentProperty(serialize(p1), serialize(p2));
	}

	@Override
	public void addTransitiveProperty(ATermAppl p) {
		service.addTransitiveProperty(serialize(p));
	}

	@Override
	public void addSame(ATermAppl i1, ATermAppl i2) {
		service.addSame(serialize(i1), serialize(i2));
	}

	@Override
	public void addSubClass(ATermAppl sub, ATermAppl sup) {
		service.addSubClass(serialize(sub), serialize(sup));
	}

	@Override
	public void addDatatype(ATerm p) {
		service.addDatatype(serialize(p));
	}

	@Override
	public boolean isConsistent() {
		return service.isConsistent();
	}

	@Override
	public boolean isSatisfiable(ATermAppl c) {
		return service.isSatisfiable(serialize(c));
	}

	@Override
	public List<Pair<ATermAppl, Set<ATermAppl>>> unfold(ATermAppl c) {
		List<Pair<ATermAppl, Set<ATermAppl>>> unfolding = new ArrayList<Pair<ATermAppl, Set<ATermAppl>>>();
		HashMap<String, String[]> map = service.unfold(serialize(c));
		if (map == null) {
			return null;
		}
		for (Entry<String, String[]> entry : map.entrySet()) {
			ATermAppl first = deserialize(entry.getKey());
			Set<ATermAppl> second = new HashSet<ATermAppl>();
			for (String value : entry.getValue()) {
				second.add(deserialize(value));
			}
			Pair<ATermAppl, Set<ATermAppl>> pair = new Pair<ATermAppl, Set<ATermAppl>>(first, second);
			unfolding.add(pair);
		}
		return unfolding;
	}

	private String serialize(ATerm term) {
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

}

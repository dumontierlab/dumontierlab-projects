package com.dumontierlab.jxta.owl.reasoner.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.tbox.impl.TBoxExpImpl;
import org.mindswap.pellet.utils.Pair;

import aterm.ATermAppl;

import com.dumontierlab.jxta.owl.dht.DistributedHashTable;
import com.dumontierlab.jxta.owl.dht.RemoteService;
import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;

public class DistributedTBoxFragment extends TBoxExpImpl {

	private static final Logger LOG = Logger.getLogger(DistributedTBoxFragment.class);

	private final DistributedHashTable<DistributedKnowledgeBaseFragment> hashTable;
	private final Set<ATermAppl> remoteClasses;
	private boolean normilized;

	public DistributedTBoxFragment(KnowledgeBase kb, DistributedHashTable<DistributedKnowledgeBaseFragment> hashTable) {
		super(kb);
		this.hashTable = hashTable;
		remoteClasses = new HashSet<ATermAppl>();
	}

	@Override
	public List<Pair<ATermAppl, Set<ATermAppl>>> unfold(ATermAppl c) {

		LOG.debug("Unfloding concept: " + c);

		if (!normilized) {
			LOG.debug("Normalizing Tbox");
			normalize();
			normilized = true;
		}

		if (isRemote(c)) {
			RemoteService<DistributedKnowledgeBaseFragment> peer = hashTable.getResponsiblePeer(c);
			return peer.getService().unfold(c);
		}

		List<Pair<ATermAppl, Set<ATermAppl>>> unfolding = super.unfold(c);
		if (unfolding == null) {
			return null;
		}

		for (Pair<ATermAppl, Set<ATermAppl>> pair : new ArrayList<Pair<ATermAppl, Set<ATermAppl>>>(unfolding)) {
			if (isRemote(pair.first)) {
				RemoteService<DistributedKnowledgeBaseFragment> peer = hashTable.getResponsiblePeer(pair.first);
				List<Pair<ATermAppl, Set<ATermAppl>>> remoteUnfolding = peer.getService().unfold(pair.first);
				if (remoteUnfolding != null) {
					unfolding.addAll(remoteUnfolding);
				}
			}
		}

		LOG.debug("result unfolding for " + c + " is: " + unfolding);
		return unfolding;
	}

	public boolean isRemote(ATermAppl c) {
		return remoteClasses.contains(c);
	}

	public void addRemoteClass(ATermAppl c2) {
		addClass(c2);
		remoteClasses.add(c2);
	}
}

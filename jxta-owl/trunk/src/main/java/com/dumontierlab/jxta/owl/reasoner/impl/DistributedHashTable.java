package com.dumontierlab.jxta.owl.reasoner.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import aterm.ATermAppl;

import com.dumontierlab.jxta.owl.dht.DhtHelper;
import com.dumontierlab.jxta.owl.dht.WorkerPeer;

public class DistributedHashTable<E> {

	private final List<WorkerPeer<E>> peers;
	private BigInteger[] peersHashes;

	public DistributedHashTable(Collection<WorkerPeer<E>> peers) {
		this.peers = new ArrayList<WorkerPeer<E>>(peers);
	}

	public Collection<WorkerPeer<E>> getPeers() {
		return peers;
	}

	public WorkerPeer<E> getResponsiblePeer(ATermAppl term) {
		BigInteger hash = DhtHelper.hash(term);

		int closest = DhtHelper.getClosest(hash, getPeersHashes());
		return peers.get(closest);
	}

	public BigInteger[] getPeersHashes() {
		if (peersHashes == null) {
			peersHashes = new BigInteger[peers.size()];
			int i = 0;
			for (WorkerPeer<?> peer : peers) {
				peersHashes[i++] = DhtHelper.hash(peer.getPeerId(), true);
			}
		}
		return peersHashes;
	}

}

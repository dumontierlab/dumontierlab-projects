package com.dumontierlab.jxta.owl.dht;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import aterm.ATermAppl;

public class DistributedHashTable<E> {

	private final List<RemoteService<E>> peers;
	private BigInteger[] peersHashes;

	public DistributedHashTable() {
		this.peers = new ArrayList<RemoteService<E>>();
	}

	public DistributedHashTable(Collection<RemoteService<E>> peers) {
		this.peers = new ArrayList<RemoteService<E>>(peers);
	}

	public Collection<RemoteService<E>> getPeers() {
		return peers;
	}

	public void addPeer(RemoteService<E> peer) {
		peers.add(peer);
		if (peersHashes != null) {
			peersHashes = Arrays.copyOf(peersHashes, peersHashes.length + 1);
			peersHashes[peersHashes.length - 1] = DhtHelper.hash(peer.getId(), true);
		}
	}

	public RemoteService<E> getResponsiblePeer(ATermAppl term) {
		BigInteger hash = DhtHelper.hash(term);

		int closest = DhtHelper.getClosest(hash, getPeersHashes());
		return peers.get(closest);
	}

	public BigInteger[] getPeersHashes() {
		if (peersHashes == null) {
			peersHashes = new BigInteger[peers.size()];
			int i = 0;
			for (RemoteService<?> peer : peers) {
				peersHashes[i++] = DhtHelper.hash(peer.getId(), true);
			}
		}
		return peersHashes;
	}

}

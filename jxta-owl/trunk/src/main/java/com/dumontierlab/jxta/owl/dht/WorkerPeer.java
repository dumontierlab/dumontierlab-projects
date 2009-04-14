package com.dumontierlab.jxta.owl.dht;


public class WorkerPeer<E> {

	private final String peerId;
	private final E service;

	public WorkerPeer(String peerId, E service) {
		this.peerId = peerId;
		this.service = service;
	}

	public String getPeerId() {
		return peerId;
	}

	public E getService() {
		return service;
	}

}

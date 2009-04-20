package com.dumontierlab.jxta.owl.dht;


public class RemoteService<E> {

	private final String id;
	private final E service;

	public RemoteService(String id, E service) {
		this.id = id;
		this.service = service;
	}

	public String getId() {
		return id;
	}

	public E getService() {
		return service;
	}

}

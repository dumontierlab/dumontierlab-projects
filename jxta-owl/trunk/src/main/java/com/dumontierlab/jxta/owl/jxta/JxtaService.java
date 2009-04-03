package com.dumontierlab.jxta.owl.jxta;

import net.jxta.peergroup.PeerGroup;
import net.jxta.soap.ServiceDescriptor;

import com.dumontierlab.jxta.owl.jxta.exception.JxtaException;

public interface JxtaService {

	PeerGroup getPeerGroup();

	void advertiseSoapService(ServiceDescriptor serviceDescriptor) throws JxtaException;

}

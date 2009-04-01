package com.dumontierlab.jxta.owl.jxta;

import net.jxta.peergroup.PeerGroup;
import net.jxta.soap.ServiceDescriptor;

public interface JxtaService {

	PeerGroup getPeerGroup();

	void advertiseSoapService(ServiceDescriptor serviceDescriptor);

}

package com.dumontierlab.jxta.owl.service.impl;

import javax.xml.rpc.ServiceException;

import net.jxta.soap.ServiceDescriptor;

import com.dumontierlab.jxta.owl.service.OWLReasonerService;

public class OWLReasonerServiceImpl implements OWLReasonerService {

	public static final ServiceDescriptor DESCRIPTOR = new ServiceDescriptor(
			"com.dumontierlab.jxta.owl.service.impl.OWLReasonerServiceImpl", // class
			"OWLReasonerServiceImpl", // name
			"0.1", // version
			"Dumontierlab", // creator
			"jxta:/dumontierlab.com/jxta-owl/service/OWLReasonerServiceImpl", // specURI
			"The simple hello service example", // description
			"urn:jxta:jxta-NetGroup", // peergroup ID
			"JXTA NetPeerGroup", // PeerGroup name
			"JXTA NetPeerGroup", // PeerGroup description
			false, // secure policy flag (use default=false)
			null); // security policy type (use no policy)

	public void test() {
		System.err.println("============ T.E.S.T =============");

	}

	/* ----------- life cycle -- */
	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void init(Object arg0) throws ServiceException {
		// TODO Auto-generated method stub

	}

}

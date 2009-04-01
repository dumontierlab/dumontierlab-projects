package com.dumontierlab.jxta.owl.diag;

import java.net.URI;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dumontierlab.jxta.owl.jxta.JxtaService;
import com.dumontierlab.jxta.owl.jxta.JxtaServiceImpl;
import com.dumontierlab.jxta.owl.jxta.exception.JxtaBootstrapException;
import com.dumontierlab.jxta.owl.service.impl.OWLReasonerServiceImpl;

public class ServerTest {

	private static final Logger LOG = Logger.getLogger(ServerTest.class.getName());

	// TODO: this is just to test the connection
	public static void main(String[] args) {
		Set<URI> seeds = Collections.singleton(URI.create("http://dsg.ce.unipr.it/research/SP2A/rdvlist.txt"));
		try {
			JxtaService jxta = new JxtaServiceImpl("testPeer-server", seeds, ".jxta");
			jxta.advertiseSoapService(OWLReasonerServiceImpl.DESCRIPTOR);

		} catch (JxtaBootstrapException e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
			System.exit(1);
		}
	}

}

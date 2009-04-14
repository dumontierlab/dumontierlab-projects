package com.dumontierlab.jxta.owl.diag;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import org.apache.log4j.Logger;

import com.dumontierlab.jxta.owl.jxta.JxtaService;
import com.dumontierlab.jxta.owl.jxta.JxtaServiceImpl;
import com.dumontierlab.jxta.owl.jxta.exception.JxtaException;
import com.dumontierlab.jxta.owl.service.DistributedKnowledgeBaseFragmentServiceImpl;

public class ServerTest {

	private static final Logger LOG = Logger.getLogger(ServerTest.class);

	// TODO: this is just to test the connection
	public static void main(String[] args) {
		Set<URI> seeds = Collections.singleton(URI.create("http://dsg.ce.unipr.it/research/SP2A/rdvlist.txt"));
		try {
			JxtaService jxta = new JxtaServiceImpl("testPeer-server", seeds, ".jxta");
			jxta.advertiseSoapService(DistributedKnowledgeBaseFragmentServiceImpl.DESCRIPTOR);

		} catch (JxtaException e) {
			LOG.fatal(e.getMessage(), e);
			System.exit(1);
		}
	}

}

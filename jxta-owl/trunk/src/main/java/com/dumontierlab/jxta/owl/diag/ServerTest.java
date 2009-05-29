package com.dumontierlab.jxta.owl.diag;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.jxta.soap.SOAPService;

import org.apache.log4j.Logger;

import com.dumontierlab.jxta.owl.Bootstrapper;
import com.dumontierlab.jxta.owl.configuration.JxtaOwlOptions;
import com.dumontierlab.jxta.owl.inject.ServiceLocator;
import com.dumontierlab.jxta.owl.jxta.JxtaService;
import com.dumontierlab.jxta.owl.jxta.exception.JxtaException;
import com.dumontierlab.jxta.owl.service.DistributedKnowledgeBaseFragmentServiceImpl;

public class ServerTest {

	private static final Logger LOG = Logger.getLogger(ServerTest.class);

	// TODO: this is just to test the connection
	public static void main(String[] args) {

		if (args.length == 1) {
			System.setProperty(JxtaOwlOptions.PEER_NAME_OPT, args[0]);
		}

		ServiceLocator locator = Bootstrapper.bootstap();

		try {
			final JxtaService jxta = locator.getJxta();
			final SOAPService service = jxta.deploySoapService(DistributedKnowledgeBaseFragmentServiceImpl.DESCRIPTOR);

			Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					service.republish();
				}
			}, 0, 10, TimeUnit.SECONDS);
		} catch (JxtaException e) {
			LOG.fatal(e.getMessage(), e);
			System.exit(1);
		}
	}

}

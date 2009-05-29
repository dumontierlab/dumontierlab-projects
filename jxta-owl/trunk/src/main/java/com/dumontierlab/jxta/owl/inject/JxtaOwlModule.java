package com.dumontierlab.jxta.owl.inject;

import java.util.Random;

import com.dumontierlab.jxta.owl.configuration.JxtaOwlOptions;
import com.dumontierlab.jxta.owl.discovery.Discovery;
import com.dumontierlab.jxta.owl.discovery.impl.DiscoveryImpl;
import com.dumontierlab.jxta.owl.inject.annotation.OptionImpl;
import com.dumontierlab.jxta.owl.jxta.JxtaService;
import com.dumontierlab.jxta.owl.jxta.JxtaServiceImpl;
import com.google.inject.AbstractModule;

public class JxtaOwlModule extends AbstractModule {

	@Override
	protected void configure() {

		// services
		bind(ServiceLocator.class);
		bind(JxtaService.class).to(JxtaServiceImpl.class);
		bind(Discovery.class).to(DiscoveryImpl.class);

		// constants
		bindConstant().annotatedWith(new OptionImpl(JxtaOwlOptions.PEER_NAME_OPT)).to(
				System.getProperty(JxtaOwlOptions.PEER_NAME_OPT, getRandomPeerName()));
		bindConstant().annotatedWith(new OptionImpl(JxtaOwlOptions.CALL_TIMEOUT_OPT)).to(
				System.getProperty(JxtaOwlOptions.CALL_TIMEOUT_OPT, "1800000"));
		bindConstant().annotatedWith(new OptionImpl(JxtaOwlOptions.DISCOVERY_TIMEOUT_OPT)).to(
				System.getProperty(JxtaOwlOptions.DISCOVERY_TIMEOUT_OPT, "120000"));
		bindConstant().annotatedWith(new OptionImpl(JxtaOwlOptions.JXTA_HOME)).to(
				System.getProperty(JxtaOwlOptions.JXTA_HOME, ".jxta"));

	}

	private String getRandomPeerName() {
		StringBuilder name = new StringBuilder();
		byte[] bytes = new byte[16];
		new Random().nextBytes(bytes);
		for (byte b : bytes) {
			name.append(Integer.toHexString(b));
		}
		return name.toString();
	}
}

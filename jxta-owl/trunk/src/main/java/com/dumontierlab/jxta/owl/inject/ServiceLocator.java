package com.dumontierlab.jxta.owl.inject;

import org.apache.log4j.Logger;

import com.dumontierlab.jxta.owl.binding.BindingFactory;
import com.dumontierlab.jxta.owl.discovery.Discovery;
import com.dumontierlab.jxta.owl.jxta.JxtaService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ServiceLocator {

	private static final Logger LOG = Logger.getLogger(ServiceLocator.class);

	private static ServiceLocator instance;

	private final JxtaService jxta;
	private final BindingFactory bindingFactory;
	private final Discovery discovery;

	@Inject
	public ServiceLocator(JxtaService jxta, BindingFactory bindingFactory, Discovery discovery) {
		LOG.debug("Creating Servive Locator");
		this.jxta = jxta;
		this.bindingFactory = bindingFactory;
		this.discovery = discovery;
		instance = this;
	}

	public static ServiceLocator getInstance() {
		assert instance != null : "Dependency injection has not been initialized yet.";
		return instance;
	}

	public JxtaService getJxta() {
		return jxta;
	}

	public BindingFactory getBindingFactory() {
		return bindingFactory;
	}

	public Discovery getDiscovery() {
		return discovery;
	}
}

package com.dumontierlab.jxta.owl.discovery.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.xml.namespace.QName;

import net.jxta.protocol.ModuleSpecAdvertisement;

import com.dumontierlab.jxta.owl.binding.BindingFactory;
import com.dumontierlab.jxta.owl.configuration.JxtaOwlOptions;
import com.dumontierlab.jxta.owl.dht.RemoteService;
import com.dumontierlab.jxta.owl.discovery.Discovery;
import com.dumontierlab.jxta.owl.inject.annotation.Option;
import com.dumontierlab.jxta.owl.jxta.JxtaService;
import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;
import com.dumontierlab.jxta.owl.service.DistributedKnowledgeBaseFragmentService;
import com.dumontierlab.jxta.owl.service.DistributedKnowledgeBaseFragmentServiceAdapter;
import com.dumontierlab.jxta.owl.service.DistributedKnowledgeBaseFragmentServiceImpl;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DiscoveryImpl implements Discovery {

	private final int discoveryTimeout;
	private final JxtaService jxta;
	private final BindingFactory bindingFactory;

	@Inject
	public DiscoveryImpl(JxtaService jxta, BindingFactory bindingFactory,
			@Option(JxtaOwlOptions.DISCOVERY_TIMEOUT_OPT) int discoveryTimeout) {
		this.jxta = jxta;
		this.bindingFactory = bindingFactory;
		this.discoveryTimeout = discoveryTimeout;
	}

	@Override
	public Collection<RemoteService<DistributedKnowledgeBaseFragment>> discoverPeers(int max)
			throws InterruptedException, TimeoutException, IOException {

		Collection<ModuleSpecAdvertisement> adversticements = jxta.discoverService(
				DistributedKnowledgeBaseFragmentServiceImpl.DESCRIPTOR.getName(), discoveryTimeout, max);

		List<RemoteService<DistributedKnowledgeBaseFragment>> peers = new ArrayList<RemoteService<DistributedKnowledgeBaseFragment>>();
		for (ModuleSpecAdvertisement advertisement : adversticements) {
			DistributedKnowledgeBaseFragmentService service = bindingFactory.getBinding(
					DistributedKnowledgeBaseFragmentService.class, getServiceNameQName(),
					DistributedKnowledgeBaseFragmentServiceImpl.DESCRIPTOR, advertisement);

			DistributedKnowledgeBaseFragment adapter = new DistributedKnowledgeBaseFragmentServiceAdapter(service);
			RemoteService<DistributedKnowledgeBaseFragment> peer = new RemoteService<DistributedKnowledgeBaseFragment>(
					advertisement.getID().toString(), adapter);
			peers.add(peer);

			for (ModuleSpecAdvertisement advertisement2 : adversticements) {
				if (advertisement != advertisement2) {
					// TODO: is there a better way to serialize the
					// advertisement?
					service.addRemoteService(advertisement2.toString());
				}
			}
		}

		return peers;

	}

	private QName getServiceNameQName() {
		// TODO: construct the URI using reflection ...
		return new QName("http://impl.service.owl.jxta.dumontierlab.com",
				DistributedKnowledgeBaseFragmentServiceImpl.DESCRIPTOR.getName());
	}
}

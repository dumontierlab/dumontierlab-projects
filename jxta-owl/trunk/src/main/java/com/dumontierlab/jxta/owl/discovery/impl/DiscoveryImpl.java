package com.dumontierlab.jxta.owl.discovery.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.xml.namespace.QName;

import net.jxta.protocol.ModuleSpecAdvertisement;

import com.dumontierlab.jxta.owl.binding.BindingFactory;
import com.dumontierlab.jxta.owl.dht.WorkerPeer;
import com.dumontierlab.jxta.owl.discovery.Discovery;
import com.dumontierlab.jxta.owl.jxta.JxtaService;
import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;
import com.dumontierlab.jxta.owl.service.DistributedKnowledgeBaseFragmentService;
import com.dumontierlab.jxta.owl.service.DistributedKnowledgeBaseFragmentServiceAdapter;
import com.dumontierlab.jxta.owl.service.DistributedKnowledgeBaseFragmentServiceImpl;

public class DiscoveryImpl implements Discovery {

	private static final long TIMEOUT = 5000; // 5sec
	private final JxtaService jxta;

	public DiscoveryImpl(JxtaService jxta) {
		this.jxta = jxta;
	}

	@Override
	public Collection<WorkerPeer<DistributedKnowledgeBaseFragment>> discoverPeers(int max) throws InterruptedException,
			TimeoutException {

		Collection<ModuleSpecAdvertisement> adversticements = jxta.discoverService(
				DistributedKnowledgeBaseFragmentServiceImpl.DESCRIPTOR.getName(), TIMEOUT, max);

		List<WorkerPeer<DistributedKnowledgeBaseFragment>> peers = new ArrayList<WorkerPeer<DistributedKnowledgeBaseFragment>>();
		for (ModuleSpecAdvertisement advertisement : adversticements) {
			DistributedKnowledgeBaseFragmentService service = BindingFactory.getBinding(
					DistributedKnowledgeBaseFragmentService.class, getServiceNameQName(),
					DistributedKnowledgeBaseFragmentServiceImpl.DESCRIPTOR, advertisement, jxta, new Long(TIMEOUT)
							.intValue());
			DistributedKnowledgeBaseFragment adapter = new DistributedKnowledgeBaseFragmentServiceAdapter(service);
			WorkerPeer<DistributedKnowledgeBaseFragment> peer = new WorkerPeer<DistributedKnowledgeBaseFragment>(
					advertisement.getID().toString(), adapter);
			peers.add(peer);
		}
		return peers;

	}

	private QName getServiceNameQName() {
		// TODO: construct the URI using reflection ...
		return new QName("http://impl.service.owl.jxta.dumontierlab.com",
				DistributedKnowledgeBaseFragmentServiceImpl.DESCRIPTOR.getName());
	}
}

package com.dumontierlab.jxta.owl.discovery;

import java.util.Collection;
import java.util.concurrent.TimeoutException;

import com.dumontierlab.jxta.owl.dht.WorkerPeer;
import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;

public interface Discovery {

	Collection<WorkerPeer<DistributedKnowledgeBaseFragment>> discoverPeers(int max) throws InterruptedException,
			TimeoutException;

}

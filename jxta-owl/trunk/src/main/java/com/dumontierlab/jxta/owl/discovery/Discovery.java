package com.dumontierlab.jxta.owl.discovery;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

import com.dumontierlab.jxta.owl.dht.RemoteService;
import com.dumontierlab.jxta.owl.reasoner.DistributedKnowledgeBaseFragment;

public interface Discovery {

	Collection<RemoteService<DistributedKnowledgeBaseFragment>> discoverPeers(int max) throws InterruptedException,
			TimeoutException, IOException;

}

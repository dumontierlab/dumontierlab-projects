package com.dumontierlab.jxta.owl.jxta;

import java.util.Collection;
import java.util.concurrent.TimeoutException;

import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.soap.SOAPService;
import net.jxta.soap.ServiceDescriptor;

import com.dumontierlab.jxta.owl.jxta.exception.JxtaException;

public interface JxtaService {

	PeerGroup getPeerGroup();

	SOAPService deploySoapService(ServiceDescriptor serviceDescriptor) throws JxtaException;

	Collection<ModuleSpecAdvertisement> discoverService(String serviceName, long timeoutMillis)
			throws InterruptedException, TimeoutException;

	Collection<ModuleSpecAdvertisement> discoverService(String serviceName, long timeoutMillis, int max)
			throws InterruptedException, TimeoutException;

}

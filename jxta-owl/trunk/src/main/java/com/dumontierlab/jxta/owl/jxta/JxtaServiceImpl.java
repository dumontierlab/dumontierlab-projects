package com.dumontierlab.jxta.owl.jxta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.XMLDocument;
import net.jxta.document.XMLElement;
import net.jxta.exception.PeerGroupException;
import net.jxta.peergroup.NetPeerGroupFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.soap.SOAPService;
import net.jxta.soap.SOAPServiceThread;
import net.jxta.soap.ServiceDescriptor;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.UrlBase64;

import com.dumontierlab.jxta.owl.configuration.JxtaOwlOptions;
import com.dumontierlab.jxta.owl.inject.annotation.Option;
import com.dumontierlab.jxta.owl.jxta.exception.JxtaBootstrapException;
import com.dumontierlab.jxta.owl.jxta.exception.JxtaException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class JxtaServiceImpl implements JxtaService {

	private static final Logger LOG = Logger.getLogger(JxtaServiceImpl.class);

	private final String peerName;
	private final String jxtaHome;
	private final Collection<URI> seedsUris;

	private volatile PeerGroup netPeerGroup;

	@Inject
	public JxtaServiceImpl(@Option(JxtaOwlOptions.PEER_NAME_OPT) String peerName,
			@Option(JxtaOwlOptions.JXTA_HOME) String jxtaHome) throws JxtaBootstrapException {
		this.peerName = peerName;
		// TODO: make this configurable.
		seedsUris = Collections.singleton(URI.create("http://dsg.ce.unipr.it/research/SP2A/rdvlist.txt"));
		this.jxtaHome = jxtaHome;
		startJxta();
	}

	public PeerGroup getPeerGroup() {
		return netPeerGroup;
	}

	@Override
	public SOAPService deploySoapService(ServiceDescriptor serviceDescriptor) throws JxtaException {
		// TODO: make lifetime and expiration a parameter for this method.
		SOAPService service = new SOAPService();

		// Build the ModuleSpecAdv 'Param' section
		XMLDocument param = (XMLDocument) StructuredDocumentFactory.newStructuredDocument(new MimeMediaType("text/xml"),
				"Parm");

		// *************** 1. Add the service WSDL description *****************
		XMLElement wsdlElem;
		try {
			wsdlElem = param.createElement("WSDL", new String(UrlBase64.encode(serviceDescriptor.getWsdl().getBytes())));
			param.appendChild(wsdlElem);
		} catch (Exception e) {
			LOG.warn("Unable to generate WSDL for service: " + serviceDescriptor.getName(), e);
		}

		// ********************* 2. Add secure pipe tag ************************
		String secure = (serviceDescriptor.isSecure()) ? "true" : "false";
		XMLElement secureElem = param.createElement("secure", secure);
		param.appendChild(secureElem);

		// Set the context object, with init parameters
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("serviceName", serviceDescriptor.getName());
		service.setContext(parameters);

		// Initialize the service
		try {
			service.init(netPeerGroup, serviceDescriptor, param, null);
		} catch (Exception e) {
			throw new JxtaException("Unable to initialize service: " + serviceDescriptor.getName(), e);
		}
		// saving msadv
		try {
			param.sendToStream(new FileOutputStream("ServiceParm.xml"));
		} catch (Exception e) {
			throw new JxtaException("Exception in saving service params section", e);
		}

		// Start a new ServiceThread running this service
		new SOAPServiceThread(service).start();

		return service;
	}

	@Override
	public Collection<ModuleSpecAdvertisement> discoverService(String serviceName, long timeoutMillis)
			throws InterruptedException, TimeoutException {
		return discoverService(serviceName, timeoutMillis, Integer.MAX_VALUE);
	}

	@Override
	public Collection<ModuleSpecAdvertisement> discoverService(String serviceName, long timeoutMillis, int max)
			throws InterruptedException, TimeoutException {
		long startTime = System.currentTimeMillis();
		List<ModuleSpecAdvertisement> serviceAdvertisements = max < Integer.MAX_VALUE ? new ArrayList<ModuleSpecAdvertisement>(
				max)
				: new ArrayList<ModuleSpecAdvertisement>();
		Set<String> discoveredAdvertisementsIds = new HashSet<String>();
		LOG.debug("Searching for service:" + serviceName);

		// Initialize Discovery Service
		DiscoveryService discoverySvc = getPeerGroup().getDiscoveryService();
		while (true) {
			long timeElapsed = System.currentTimeMillis() - startTime;
			if (timeElapsed > timeoutMillis) {
				if (serviceAdvertisements.size() == 0) {
					throw new TimeoutException("Discover service " + serviceName + " timeout after " + timeElapsed
							+ " ms.");
				} else {
					return serviceAdvertisements;
				}
			}
			try {
				System.out.println("Looking for local advertisements...");
				Enumeration<Advertisement> advs = discoverySvc.getLocalAdvertisements(DiscoveryService.ADV, "Name",
						serviceName);
				if (advs != null && advs.hasMoreElements()) {
					while (advs.hasMoreElements()) {
						// Make sure it is a ModuleSpecAdvertisement (we will
						// also find ModuleClass and Pipe advertisements)
						Advertisement tempAdv = advs.nextElement();
						if (tempAdv instanceof ModuleSpecAdvertisement
								&& !discoveredAdvertisementsIds.contains(tempAdv.getID().toString())) {
							LOG.debug("Found advertisement for " + serviceName + " in cache.");
							serviceAdvertisements.add((ModuleSpecAdvertisement) tempAdv);
							discoveredAdvertisementsIds.add(tempAdv.getID().toString());
						}
					}
					if (serviceAdvertisements.size() == max) {
						break;
					}
				}
				LOG.debug("Looking remotely for service advertisement: " + serviceName);
				discoverySvc.getRemoteAdvertisements(null, DiscoveryService.ADV, "Name", serviceName, max, null);
				Thread.sleep(500);
			} catch (IOException e) {
				LOG.debug("Found nothing! for:" + serviceName, e);
			}
			discoverySvc.getRemoteAdvertisements(null, DiscoveryService.ADV, "Name", serviceName, max
					- serviceAdvertisements.size());
		}
		return serviceAdvertisements;
	}

	protected void startJxta() throws JxtaBootstrapException {
		NetworkConfigurator configuration = new NetworkConfigurator();
		File home = new File(jxtaHome, peerName);
		configuration.setHome(home);

		boolean needToConfigure = configuration.exists();

		if (!needToConfigure) {
			try {
				configuration.load();
			} catch (Exception e) {
				LOG.warn("Unable to load configuration, generating new configuration.", e);
				needToConfigure = true;
			}
		}

		if (needToConfigure) {
			configuration.setName(peerName);
			// TODO: What is the right node type to use?
			configuration.setMode(NetworkConfigurator.RDV_NODE);
			configuration.setUseMulticast(true);
			for (URI seed : seedsUris) {
				configuration.addRdvSeedingURI(seed);
			}

			// try {
			// configuration.save();
			// } catch (IOException e) {
			// LOG.warn("Unable to save peer configuration.", e);
			// }
		}

		try {
			NetPeerGroupFactory factory = new NetPeerGroupFactory(configuration.getPlatformConfig(), home.toURI());
			netPeerGroup = factory.getInterface();

			LOG.info("My JXTA PeerID is: " + netPeerGroup.getPeerID().getUniqueValue().toString());

		} catch (PeerGroupException e) {
			throw new JxtaBootstrapException("Unable to bootstrap JXTA: " + e.getMessage(), e);
		}

	}

}

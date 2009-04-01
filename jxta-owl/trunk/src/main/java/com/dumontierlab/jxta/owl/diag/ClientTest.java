package com.dumontierlab.jxta.owl.diag;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.TextElement;
import net.jxta.protocol.ModuleSpecAdvertisement;

import org.bouncycastle.util.encoders.UrlBase64;

import com.dumontierlab.jxta.owl.jxta.JxtaService;
import com.dumontierlab.jxta.owl.jxta.JxtaServiceImpl;
import com.dumontierlab.jxta.owl.jxta.exception.JxtaBootstrapException;
import com.dumontierlab.jxta.owl.service.impl.OWLReasonerServiceImpl;

public class ClientTest {

	private static final Logger LOG = Logger.getLogger(ClientTest.class.getName());

	// TODO: this is just to test the connection
	public static void main(String[] args) {
		Set<URI> seeds = Collections.singleton(URI.create("http://dsg.ce.unipr.it/research/SP2A/rdvlist.txt"));
		try {
			JxtaService jxta = new JxtaServiceImpl("testPeer-client", seeds, ".jxta");
			findService(jxta, OWLReasonerServiceImpl.DESCRIPTOR.getName());

		} catch (JxtaBootstrapException e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
			System.exit(1);
		}
	}

	/**
	 * Utility method for discovering service advertisements
	 */
	private static ModuleSpecAdvertisement discoverServices(JxtaService jxta, String serviceName) {
		int found = 0;
		int timeout = 500;
		Object tempAdv;
		Vector<ModuleSpecAdvertisement> serviceAdvs = new Vector<ModuleSpecAdvertisement>();

		System.out.println("Searching for 'HelloService'");
		// Initialize Discovery Service
		DiscoveryService discoverySvc = jxta.getPeerGroup().getDiscoveryService();
		long startSVCsearch = System.nanoTime();
		while (true) {
			try {
				System.out.println("Looking for local advertisements...");
				Enumeration advs = discoverySvc.getLocalAdvertisements(DiscoveryService.ADV, "Name", serviceName);
				if (advs != null && advs.hasMoreElements()) {
					while (advs.hasMoreElements()) {
						// Make sure it is a ModuleSpecAdvertisement (we will
						// also find
						// ModuleClass and Pipe advertisements)
						if ((tempAdv = advs.nextElement()) instanceof ModuleSpecAdvertisement) {
							System.out.println("Found advertisement in cache, adding to list");
							serviceAdvs.addElement((ModuleSpecAdvertisement) tempAdv);
							++found;
						}
					}
					if (found > 0) {
						long timeSVCsearch = System.nanoTime() - startSVCsearch;
						System.out.println("Time for discovering a service " + timeSVCsearch + " nsec");
						break;
					}
				}
				System.out.println("Looking for remote advertisements...");
				discoverySvc.getRemoteAdvertisements(null, DiscoveryService.ADV, "Name", serviceName, 5, null);
				try {
					Thread.sleep(timeout);
				} catch (InterruptedException e) {
				}
			} catch (IOException e) {
				// Found nothing! move on
			}
		}

		System.out.println("Found " + found + " advertisements(s).");

		Object element = serviceAdvs.firstElement();
		if (!(element instanceof ModuleSpecAdvertisement)) {
			System.out.println("Something wrong with ModuleSpecAdv");
			System.exit(1);
		}

		return serviceAdvs.firstElement();
	}

	/**
	 * Discovering the specified service module spec adv (by Name) and
	 * extracting WSDL service description
	 */
	private static void findService(JxtaService jxta, String serviceName) {
		System.out.println("\n### Find remote HelloService ###");

		// Look for service advertisements
		ModuleSpecAdvertisement msadv = discoverServices(jxta, serviceName);

		String WSDLbuffer = null;
		String decodedWSDL = null;
		// Print out the params of the service advertisement we found
		try {
			StructuredTextDocument param = (StructuredTextDocument) msadv.getParam();

			try {
				System.out.println("\nSaving to file service param fields...");
				param.sendToStream(new FileOutputStream("ServiceParm.xml"));
			} catch (Exception e) {
				System.out.println("Exception in saving service params section!");
				e.printStackTrace();
			}

			// Now get all fields
			Enumeration params = param.getChildren();
			TextElement elem;
			String testtext = new String();
			while (params != null && params.hasMoreElements()) {
				elem = (TextElement) params.nextElement();
				// Check for WSDL service description
				if (elem.getName().equals("WSDL")) {
					WSDLbuffer = new String(elem.getTextValue());
					try {
						decodedWSDL = new String(UrlBase64.decode(WSDLbuffer.getBytes()));
					} catch (Exception ce) {
						ce.printStackTrace();
						System.exit(1);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error extracting service advertisement");
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Print out decoded WSDL content:");
		System.out.println(decodedWSDL);

		System.out.println("\nCheck service invocation policy");
		System.out.println("=========================================");
		System.out.println("-> Setting unsecure service invocation...");
		System.out.println("=========================================");
	}

}

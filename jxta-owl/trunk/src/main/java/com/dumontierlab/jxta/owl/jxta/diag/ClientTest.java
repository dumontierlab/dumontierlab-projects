package com.dumontierlab.jxta.owl.jxta.diag;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.TextElement;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.soap.CallFactory;
import net.jxta.soap.ServiceDescriptor;
import net.jxta.soap.deploy.SOAPTransportDeployer;

import org.apache.axis.client.Call;
import org.bouncycastle.util.encoders.UrlBase64;

import com.dumontierlab.jxta.owl.jxta.JxtaService;
import com.dumontierlab.jxta.owl.jxta.JxtaServiceImpl;
import com.dumontierlab.jxta.owl.service.impl.OWLReasonerServiceImpl;

public class ClientTest {

	private static final Logger LOG = Logger.getLogger(ClientTest.class.getName());

	// TODO: this is just to test the connection
	public static void main(String[] args) {
		Set<URI> seeds = Collections.singleton(URI.create("http://dsg.ce.unipr.it/research/SP2A/rdvlist.txt"));
		try {
			JxtaService jxta = new JxtaServiceImpl("testPeer-client", seeds, ".jxta");
			ModuleSpecAdvertisement msadv = discoverServices(jxta, OWLReasonerServiceImpl.DESCRIPTOR.getName());
			interactWithService(msadv, OWLReasonerServiceImpl.DESCRIPTOR, jxta);

		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
			System.exit(1);
		}
	}

	private static void interactWithService(ModuleSpecAdvertisement msadv, ServiceDescriptor descriptor, JxtaService jxta)
			throws Exception {
		// Deploy SOAPTransport
		String wsdl = getWsdl(msadv);
		System.out.println(wsdl);

		new SOAPTransportDeployer().deploy();

		InputStream wsdlInputStream = new ByteArrayInputStream(wsdl.getBytes());

		// create call
		Call call = CallFactory.getInstance().createCall(descriptor, msadv.getPipeAdvertisement(), jxta.getPeerGroup(),
				wsdlInputStream, new QName("http://impl.service.owl.jxta.dumontierlab.com", "OWLReasonerServiceImpl"), // servicename
				new QName("http://impl.service.owl.jxta.dumontierlab.com", "OWLReasonerServiceImpl")); // portname

		call.setOperationName("test");
		call.setTimeout(new Integer(20000));
		for (int i = 0; i < 20; i++) {
			System.out.println("calling service");
			call.invoke(new Object[] {});
		}

	}

	private static String getWsdl(ModuleSpecAdvertisement msadv) {
		String wsdlBuffer = null;
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
					wsdlBuffer = new String(elem.getTextValue());
					decodedWSDL = new String(UrlBase64.decode(wsdlBuffer.getBytes()));

				}
			}
		} catch (Exception e) {
			System.out.println("Error extracting service advertisement");
			e.printStackTrace();
			System.exit(1);
		}
		return decodedWSDL;
	}

	/**
	 * Utility method for discovering service advertisements
	 */
	private static ModuleSpecAdvertisement discoverServices(JxtaService jxta, String serviceName) {
		int found = 0;
		int timeout = 500;
		Object tempAdv;
		Vector<ModuleSpecAdvertisement> serviceAdvs = new Vector<ModuleSpecAdvertisement>();

		System.out.println("Searching for " + serviceName);
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

}

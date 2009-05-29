package com.dumontierlab.jxta.owl.binding;

import java.lang.reflect.Proxy;

import javax.xml.namespace.QName;

import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.soap.ServiceDescriptor;

import com.dumontierlab.jxta.owl.configuration.JxtaOwlOptions;
import com.dumontierlab.jxta.owl.inject.annotation.Option;
import com.dumontierlab.jxta.owl.jxta.JxtaService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class BindingFactory {

	private final JxtaService jxta;
	private final int callTimeout;

	@Inject
	public BindingFactory(JxtaService jxta, @Option(JxtaOwlOptions.CALL_TIMEOUT_OPT) int callTimeout) {
		this.jxta = jxta;
		this.callTimeout = callTimeout;
	}

	@SuppressWarnings("unchecked")
	public <E> E getBinding(Class<E> c, QName serviceName, ServiceDescriptor serviceDescriptor,
			ModuleSpecAdvertisement moduleSpecAdvertisement) {
		return (E) Proxy.newProxyInstance(c.getClassLoader(), new Class<?>[] { c }, new ServiceInvocationHandler(
				serviceName, serviceDescriptor, moduleSpecAdvertisement, jxta, callTimeout));
	}
}

package com.dumontierlab.jxta.owl.binding;

import java.lang.reflect.Proxy;

import javax.xml.namespace.QName;

import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.soap.ServiceDescriptor;

import com.dumontierlab.jxta.owl.jxta.JxtaService;

public class BindingFactory {

	@SuppressWarnings("unchecked")
	public static <E> E getBinding(Class<E> c, QName serviceName, ServiceDescriptor serviceDescriptor,
			ModuleSpecAdvertisement moduleSpecAdvertisement, JxtaService jxta, int timeout) {
		return (E) Proxy.newProxyInstance(c.getClassLoader(), new Class<?>[] { c }, new ServiceInvocationHandler(
				serviceName, serviceDescriptor, moduleSpecAdvertisement, jxta, timeout));
	}
}

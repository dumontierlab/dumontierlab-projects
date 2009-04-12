package com.dumontierlab.jxta.owl.binding;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.soap.CallFactory;
import net.jxta.soap.ServiceDescriptor;

import org.apache.axis.client.Call;

import com.dumontierlab.jxta.owl.jxta.JxtaService;

public class ServiceInvocationHandler implements InvocationHandler {

	private final QName serviceName;
	private final ServiceDescriptor serviceDescriptor;
	private final ModuleSpecAdvertisement moduleSpecAdvertisement;
	private final JxtaService jxta;
	private final int timeout;

	public ServiceInvocationHandler(QName serviceName, ServiceDescriptor serviceDescriptor,
			ModuleSpecAdvertisement moduleSpecAdvertisement, JxtaService jxta, int timeout) {
		this.serviceDescriptor = serviceDescriptor;
		this.serviceName = serviceName;
		this.moduleSpecAdvertisement = moduleSpecAdvertisement;
		this.jxta = jxta;
		this.timeout = timeout;
	}

	public QName getServiceName() {
		return serviceName;
	}

	public ServiceDescriptor getServiceDescriptor() {
		return serviceDescriptor;
	}

	public ModuleSpecAdvertisement getModuleSpecAdvertisement() {
		return moduleSpecAdvertisement;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Call call = createCall();
		call.setOperation(method.getName());
		call.setTimeout(timeout);
		return call.invoke(args);
	}

	private Call createCall() throws Exception {
		InputStream wsdlInputStream = new ByteArrayInputStream(serviceDescriptor.getWsdl().getBytes());

		Call call = CallFactory.getInstance().createCall(serviceDescriptor,
				moduleSpecAdvertisement.getPipeAdvertisement(), jxta.getPeerGroup(), wsdlInputStream, serviceName,
				serviceName);
		return call;
	}
}

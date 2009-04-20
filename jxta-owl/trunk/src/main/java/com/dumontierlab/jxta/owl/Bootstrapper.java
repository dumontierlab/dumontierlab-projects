package com.dumontierlab.jxta.owl;

import com.dumontierlab.jxta.owl.inject.JxtaOwlModule;
import com.dumontierlab.jxta.owl.inject.ServiceLocator;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Bootstrapper {

	public static ServiceLocator bootstap() {
		Injector injector = Guice.createInjector(new JxtaOwlModule());
		return injector.getInstance(ServiceLocator.class);
	}

}

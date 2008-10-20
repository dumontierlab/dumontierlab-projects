package com.dumontierlab.ontocreator.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class InjectorHelper {

	private static Injector injector;

	public static void inject(Object obj) {
		if (injector == null) {
			initialize();
		}
		injector.injectMembers(obj);
	}

	private static void initialize() {
		injector = Guice.createInjector(new OntoCreatorModule());
	}

}

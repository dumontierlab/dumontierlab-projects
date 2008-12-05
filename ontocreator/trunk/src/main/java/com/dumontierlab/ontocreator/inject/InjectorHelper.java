package com.dumontierlab.ontocreator.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class InjectorHelper {

	private static Injector injector;

	public static void inject(Object obj) {
		getInjector().injectMembers(obj);
	}

	public static <E> E getInstance(Class<E> c) {
		return getInjector().getInstance(c);
	}

	private static Injector getInjector() {
		if (injector == null) {
			injector = Guice.createInjector(new OntoCreatorModule());
		}
		return injector;
	}

}

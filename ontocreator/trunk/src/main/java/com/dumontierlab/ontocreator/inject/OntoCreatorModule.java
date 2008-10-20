package com.dumontierlab.ontocreator.inject;

import com.dumontierlab.ontocreator.engine.OntoCreatorEngine;
import com.dumontierlab.ontocreator.engine.OntoCreatorEngineImpl;
import com.google.inject.AbstractModule;

public class OntoCreatorModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(OntoCreatorEngine.class).to(OntoCreatorEngineImpl.class);
	}

}

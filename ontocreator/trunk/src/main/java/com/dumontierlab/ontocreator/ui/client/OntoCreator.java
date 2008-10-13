package com.dumontierlab.ontocreator.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class OntoCreator implements EntryPoint {

	public void onModuleLoad() {
		OntoCreatorLayout ontoLayout = new OntoCreatorLayout();
		ontoLayout.setSize("100%", "100%");
		RootPanel.get().add(ontoLayout);
	}
}

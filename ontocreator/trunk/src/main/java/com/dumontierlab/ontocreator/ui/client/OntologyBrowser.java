package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.view.OntologyBrowserView;
import com.google.gwt.user.client.ui.Composite;

public class OntologyBrowser extends Composite {

	private final OntologyBrowserView view;

	public OntologyBrowser() {
		view = new OntologyBrowserView(this);
		initWidget(view);
	}
}

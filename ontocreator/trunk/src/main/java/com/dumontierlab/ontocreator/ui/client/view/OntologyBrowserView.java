package com.dumontierlab.ontocreator.ui.client.view;

import com.dumontierlab.ontocreator.ui.client.OntologyBrowser;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Shadow;

public class OntologyBrowserView extends Composite {

	private final OntologyBrowser controller;

	public OntologyBrowserView(OntologyBrowser controller) {
		this.controller = controller;
		initWidget(createUi());
	}

	private Widget createUi() {
		SimplePanel panel = new SimplePanel();

		Panel container = new Panel();
		container.setPaddings(20);

		Panel notePanel = new Panel("Note");
		notePanel.setPaddings(5);
		notePanel.add(new Label("Sorry, this feature is not implemented in this release."));
		notePanel.setHeight(150);
		notePanel.setWidth(150);
		notePanel.setShadow(Shadow.DROP);
		notePanel.setShadow(true);
		container.add(notePanel);

		container.setSize("100%", "100%");
		panel.setWidget(container);

		return panel;
	}

}

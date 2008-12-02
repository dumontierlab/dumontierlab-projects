package com.dumontierlab.ontocreator.ui.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Shadow;

public class OntologyBrowserView extends Composite {

	public OntologyBrowserView() {
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
		notePanel.setShadow(true);
		notePanel.setShadow(Shadow.DROP);
		notePanel.setShadowOffset(2);
		container.add(notePanel);

		container.setSize("100%", "100%");
		panel.setWidget(container);

		return panel;
	}

}

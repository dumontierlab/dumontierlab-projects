package com.dumontierlab.ontocreator.ui.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;

public class OntoCreatorLayout extends Composite {

	public OntoCreatorLayout() {
		initWidget(createUi());
	}

	private Widget createUi() {
		DockPanel panel = new DockPanel();
		panel.add(new OntoCreatorMenuController().getMenu(), DockPanel.NORTH);
		return panel;
	}

}

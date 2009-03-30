package com.dumontierlab.ontocreator.ui.client;

import com.google.gwt.user.client.ui.Widget;

public interface WizardTab {

	String getTabName();

	String getTabCaption();

	void initialize();

	Widget getWidget();
}

package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.view.TabFileWizardView;
import com.google.gwt.user.client.ui.Composite;

public class TabFileWizard extends Composite {

	private final TabFileWizardView view;

	public TabFileWizard() {
		view = new TabFileWizardView();
		initWidget(view);
	}

}

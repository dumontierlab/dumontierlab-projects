package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.view.EntitiesTabsView;
import com.google.gwt.user.client.ui.Composite;

public class EntitiesTabs extends Composite {

	private final EntitiesTabsView view;

	public EntitiesTabs() {
		view = new EntitiesTabsView(this);
		initWidget(view);
	}

}

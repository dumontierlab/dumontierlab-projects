package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.view.EntitiesTabsView;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class EntitiesTabs extends Composite {

	private final EntitiesTabsView view;

	public EntitiesTabs(Widget classesView, Widget propertiesView, Widget individualsView) {
		view = new EntitiesTabsView(classesView, propertiesView, individualsView);
		initWidget(view);
	}

}

package com.dumontierlab.ontocreator.ui.client.view;

import com.dumontierlab.ontocreator.ui.client.EntitiesTabs;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;

public class EntitiesTabsView extends Composite {

	private final EntitiesTabs controller;

	public EntitiesTabsView(Widget classesView, Widget propertiesView, Widget individualsView, EntitiesTabs controller) {
		this.controller = controller;
		initWidget(createUi(classesView, propertiesView, individualsView));
	}

	private Widget createUi(Widget classesView, Widget propertiesView, Widget individualsView) {
		TabPanel tabs = new TabPanel();
		tabs.setResizeTabs(true);
		tabs.setMinTabWidth(75);
		tabs.setTabWidth(100);
		tabs.setEnableTabScroll(true);

		Panel classPanel = new Panel("Classes");
		classPanel.add(classesView);
		tabs.add(classPanel);

		Panel propertiesPanel = new Panel("Properties");
		propertiesPanel.add(propertiesView);
		tabs.add(propertiesPanel);

		Panel individualPanel = new Panel("Individuals");
		individualPanel.add(individualsView);
		tabs.add(individualPanel);

		return tabs;
	}

}

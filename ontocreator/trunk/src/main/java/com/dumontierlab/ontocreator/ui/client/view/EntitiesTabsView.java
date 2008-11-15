package com.dumontierlab.ontocreator.ui.client.view;

import com.dumontierlab.ontocreator.ui.client.ClassTree;
import com.dumontierlab.ontocreator.ui.client.EntitiesTabs;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;

public class EntitiesTabsView extends Composite {

	private final EntitiesTabs controller;

	public EntitiesTabsView(EntitiesTabs controller) {
		this.controller = controller;
		initWidget(createUi());
	}

	private Widget createUi() {
		TabPanel tabs = new TabPanel();
		tabs.setResizeTabs(true);
		tabs.setMinTabWidth(75);
		tabs.setTabWidth(100);
		tabs.setEnableTabScroll(true);

		Panel classPanel = new Panel("Classes");
		classPanel.add(new ClassTree());
		tabs.add(classPanel);

		Panel propertiesPanel = new Panel("Properties");
		tabs.add(propertiesPanel);

		Panel individualPanel = new Panel("Individuals");
		tabs.add(individualPanel);

		return tabs;
	}

}

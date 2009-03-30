package com.dumontierlab.ontocreator.ui.client.view;

import com.dumontierlab.ontocreator.ui.client.ColumnRelationships;
import com.dumontierlab.ontocreator.ui.client.ColumnsDefinitions;
import com.dumontierlab.ontocreator.ui.client.WizardTab;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabFileWizardView extends Composite {

	private WizardTab[] tabs;

	public TabFileWizardView() {
		createTabs();
		initWidget(createUi());
	}

	private void createTabs() {
		tabs = new WizardTab[] { new ColumnsDefinitions(), new ColumnRelationships() };
	}

	private Widget createUi() {
		TabPanel steps = new TabPanel();
		steps.setSize("100%", "100%");

		for (WizardTab tab : tabs) {
			steps.add(tab.getWidget(), tab.getTabName());
		}

		steps.addTabListener(new TabListener() {
			public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
				// TODO Auto-generated method stub
				return true;
			}

			public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
				tabs[tabIndex].initialize();
			}
		});

		return steps;
	}

}

package com.dumontierlab.ontocreator.ui.client;

import java.util.HashSet;
import java.util.Set;

import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingBean;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabFileWizard extends Composite {

	private static final String STYLE_NAME = "tabFileWizard";

	private WizardTab[] tabs;
	private final Button nextButton;
	private final Button previousButton;
	private final TabPanel steps;
	private int stepsCompleted;

	public TabFileWizard() {
		steps = new TabPanel();
		nextButton = createNextButton();
		previousButton = createPreviousButton();
		createTabs();
		initWidget(createUi());

		steps.selectTab(0);
	}

	protected void next() {
		final int selectedTab = steps.getTabBar().getSelectedTab();
		tabs[selectedTab].complete(new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				GWT.log(caught.getMessage(), caught);
			}

			public void onSuccess(Boolean result) {
				if (result) {
					stepsCompleted++;
					int index = selectedTab + 1;
					steps.selectTab(index);
					if (index == 0) {
						nextButton.setText("Next");
						previousButton.setVisible(false);
					} else if (index == tabs.length - 1) {
						nextButton.setText("Done");
						previousButton.setVisible(true);
					} else {
						nextButton.setText("Next");
						previousButton.setVisible(true);
					}
				}
			}
		});

	}

	private void createTabs() {
		Set<ColumnMappingBean> mappings = new HashSet<ColumnMappingBean>();
		tabs = new WizardTab[] { new TabFileWizardInputs(), new ColumnsDefinitions(mappings),
				new ColumnRelationships(mappings) };
	}

	private Widget createUi() {
		steps.setWidth("100%");

		for (WizardTab tab : tabs) {
			steps.add(tab.getWidget(), tab.getTabName());
		}

		steps.addTabListener(new TabListener() {
			public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
				if (tabIndex <= stepsCompleted) {
					return true;
				} else if (tabIndex == stepsCompleted + 1) {
					next();
				}
				return false;
			}

			public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
				tabs[tabIndex].initialize();
			}
		});

		FlowPanel panel = new FlowPanel();
		panel.addStyleName(STYLE_NAME);
		panel.add(steps);

		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.setWidth("100%");
		buttonsPanel.setSpacing(5);
		buttonsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		Spacer emptySpace = new Spacer("1px", "1px");
		buttonsPanel.add(emptySpace);
		buttonsPanel.setCellWidth(emptySpace, "100%");
		buttonsPanel.add(createPreviousButton());
		buttonsPanel.add(createNextButton());
		panel.add(buttonsPanel);

		ScrollPanel scroll = new ScrollPanel(panel);
		return scroll;
	}

	private Button createNextButton() {
		return new Button("Next", new ClickListener() {
			public void onClick(Widget sender) {
				next();
			}
		});
	}

	private Button createPreviousButton() {
		return new Button("Previous", new ClickListener() {
			public void onClick(Widget sender) {
				steps.selectTab(steps.getTabBar().getSelectedTab() - 1);
			}
		});
	}

}

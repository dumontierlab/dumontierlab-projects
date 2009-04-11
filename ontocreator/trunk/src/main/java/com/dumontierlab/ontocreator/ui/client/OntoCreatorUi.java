package com.dumontierlab.ontocreator.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

public class OntoCreatorUi implements EntryPoint {

	public void onModuleLoad() {
		Panel mainPanel = new Panel();
		mainPanel.setBorder(false);
		mainPanel.setPaddings(0);
		mainPanel.setLayout(new FitLayout());

		Panel borderPanel = new Panel();
		borderPanel.setLayout(new BorderLayout());

		Panel menuPanel = new Panel();
		menuPanel.add(new OntoCreatorMenu());
		menuPanel.setHeight(32);
		borderPanel.add(menuPanel, new BorderLayoutData(RegionPosition.NORTH));

		Panel inputPanel = createInputPanel();
		inputPanel.setWidth("300px");

		BorderLayoutData westLayoutData = new BorderLayoutData(RegionPosition.WEST);
		westLayoutData.setMinSize(100);
		westLayoutData.setMaxSize(600);
		westLayoutData.setSplit(true);

		borderPanel.add(inputPanel, westLayoutData);

		Panel wizardPanel = new Panel();
		wizardPanel.setLayout(new FitLayout());

		TabFileWizard wizard = new TabFileWizard();
		wizard.setSize("100%", "100%");
		wizardPanel.add(wizard);

		borderPanel.add(wizardPanel, new BorderLayoutData(RegionPosition.CENTER));

		BorderLayoutData eastLayoutData = new BorderLayoutData(RegionPosition.EAST);
		eastLayoutData.setMinSize(100);
		eastLayoutData.setMaxSize(600);
		eastLayoutData.setSplit(true);

		mainPanel.add(borderPanel);

		new Viewport(mainPanel);
	}

	private Panel createInputPanel() {
		Panel panel = new Panel("INPUT");
		panel.setLayout(new FitLayout());
		panel.setCollapsible(true);

		Panel verticalPanel = new Panel();
		verticalPanel.setLayout(new BorderLayout());

		Panel listPanel = new Panel();
		listPanel.setLayout(new FitLayout());
		listPanel.add(new OntologiesList());
		listPanel.setHeight(100);

		BorderLayoutData borderLayoutData = new BorderLayoutData(RegionPosition.NORTH);
		borderLayoutData.setSplit(true);
		verticalPanel.add(listPanel, borderLayoutData);

		Panel entititesPanel = new Panel();
		entititesPanel.setLayout(new FitLayout());
		EntitiesTabs tabs = new EntitiesTabs(new InputClassTree(), new InputPropertyTree(), new InputIndividualList());
		entititesPanel.add(tabs);

		verticalPanel.add(entititesPanel, new BorderLayoutData(RegionPosition.CENTER));

		panel.add(verticalPanel);
		return panel;
	}

}

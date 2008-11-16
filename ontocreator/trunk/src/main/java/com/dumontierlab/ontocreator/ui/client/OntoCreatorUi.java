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
		inputPanel.setWidth("25%");

		BorderLayoutData westLayoutData = new BorderLayoutData(RegionPosition.WEST);
		westLayoutData.setMinSize(100);
		westLayoutData.setMaxSize(600);
		westLayoutData.setSplit(true);

		borderPanel.add(inputPanel, westLayoutData);

		borderPanel.add(new Spacer(), new BorderLayoutData(RegionPosition.CENTER));

		Panel outputPanel = createOutputPanel();
		outputPanel.setWidth("25%");

		BorderLayoutData eastLayoutData = new BorderLayoutData(RegionPosition.EAST);
		eastLayoutData.setMinSize(100);
		eastLayoutData.setMaxSize(600);
		eastLayoutData.setSplit(true);

		borderPanel.add(outputPanel, eastLayoutData);

		mainPanel.add(borderPanel);
		Viewport viewport = new Viewport(mainPanel);
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
		entititesPanel.add(new EntitiesTabs());

		verticalPanel.add(entititesPanel, new BorderLayoutData(RegionPosition.CENTER));

		panel.add(verticalPanel);
		return panel;
	}

	private Panel createOutputPanel() {
		Panel panel = new Panel("OUTPUT");
		panel.setLayout(new FitLayout());
		panel.setCollapsible(true);
		panel.add(new EntitiesTabs());

		return panel;
	}
}

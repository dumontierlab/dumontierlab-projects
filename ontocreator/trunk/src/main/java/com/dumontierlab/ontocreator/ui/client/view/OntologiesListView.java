package com.dumontierlab.ontocreator.ui.client.view;

import com.dumontierlab.ontocreator.ui.client.OntologiesList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;

public class OntologiesListView extends Composite {

	private final OntologiesList controller;

	public OntologiesListView(OntologiesList controller) {
		this.controller = controller;
		initWidget(createUi());
	}

	private Widget createUi() {
		SimplePanel panel = new SimplePanel();

		GridPanel list = new GridPanel(controller.getStore(), getColumModel());
		list.hideColumnHeader();
		list.getView().setAutoFill(true);
		list.getView().setForceFit(true);

		panel.setWidget(list);
		return panel;
	}

	private ColumnModel getColumModel() {
		ColumnConfig columnConfig = new ColumnConfig("Ontologies", OntologiesList.ONTOLOGY_NAME_DATA_INDEX);
		return new ColumnModel(new BaseColumnConfig[] { columnConfig });
	}

}

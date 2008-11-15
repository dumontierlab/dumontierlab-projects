package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.view.OntologiesListView;
import com.google.gwt.user.client.ui.Composite;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;

public class OntologiesList extends Composite {

	public static final String ONTOLOGY_NAME_DATA_INDEX = "ontologyName";
	private static final RecordDef recordDef = new RecordDef(new FieldDef[] { new StringFieldDef(
			ONTOLOGY_NAME_DATA_INDEX) });

	private final OntologiesListView view;
	private final Store store;

	public OntologiesList() {
		store = new Store(new ArrayReader(recordDef));
		view = new OntologiesListView(this);
		initWidget(view);
	}

	public Store getStore() {
		return store;
	}

}

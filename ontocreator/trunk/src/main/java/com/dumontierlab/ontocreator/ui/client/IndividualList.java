package com.dumontierlab.ontocreator.ui.client;

import java.util.List;

import com.dumontierlab.ontocreator.ui.client.model.OWLIndividualBean;
import com.dumontierlab.ontocreator.ui.client.view.IndividualListView;
import com.google.gwt.user.client.ui.Composite;

public class IndividualList extends Composite {

	private final IndividualListView view;

	public IndividualList() {
		view = new IndividualListView();
		initWidget(view);
	}

	public void setIndividuals(List<OWLIndividualBean> individuals) {
		view.setModel(individuals);
	}

}

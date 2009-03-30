package com.dumontierlab.ontocreator.ui.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ColumnRelationships extends Composite implements WizardTab {

	public ColumnRelationships() {
		initWidget(createUi());
	}

	private Widget createUi() {
		return new SimplePanel();
	}

	public String getTabName() {
		return "Step 3: Inter-column relationships";
	}

	public String getTabCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public Widget getWidget() {
		return this;
	}

}

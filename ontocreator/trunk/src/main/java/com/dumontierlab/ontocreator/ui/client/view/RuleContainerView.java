package com.dumontierlab.ontocreator.ui.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;

public class RuleContainerView extends Composite {

	private Panel panel;

	public RuleContainerView() {
		initWidget(createUi());
	}

	private Widget createUi() {

		return new Label("rule...");
	}

}

package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.view.RuleContainerView;
import com.google.gwt.user.client.ui.Composite;

public class RuleContainer extends Composite {

	private final RuleContainerView view;

	public RuleContainer() {
		view = new RuleContainerView();
		initWidget(view);
	}

}

package com.dumontierlab.ontocreator.ui.client.view;

import com.dumontierlab.ontocreator.ui.client.RulesEditor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;

public class RulesEditorView extends Composite {

	private final RulesEditor controller;

	public RulesEditorView(RulesEditor controller) {
		this.controller = controller;
		initWidget(createUi());
	}

	private Widget createUi() {

		Panel container = new Panel();
		container.setTopToolbar(createToolBar());

		SimplePanel gwtPanel = new SimplePanel();
		gwtPanel.setWidget(container);

		return gwtPanel;
	}

	private Toolbar createToolBar() {
		Toolbar toolbar = new Toolbar();

		ToolbarButton newRuleButton = new ToolbarButton("New rule");
		toolbar.addButton(newRuleButton);

		return toolbar;
	}

}

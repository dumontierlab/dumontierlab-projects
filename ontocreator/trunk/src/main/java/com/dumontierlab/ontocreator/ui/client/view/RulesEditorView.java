package com.dumontierlab.ontocreator.ui.client.view;

import com.dumontierlab.ontocreator.ui.client.RulesEditor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.AccordionLayout;

public class RulesEditorView extends Composite {

	private int counter = 1;
	private final RulesEditor controller;
	private Panel panel;

	public RulesEditorView(RulesEditor controller) {
		this.controller = controller;
		initWidget(createUi());
	}

	public void add(Widget ruleWidget) {
		Panel rulePanel = new Panel("Rule " + counter++);
		rulePanel.setHeight(100);
		rulePanel.add(ruleWidget);
		panel.add(rulePanel);
		panel.doLayout();
		rulePanel.expand(true);

	}

	private Widget createUi() {
		panel = new Panel();
		panel.setTopToolbar(createToolBar());
		panel.setPaddings(8);
		panel.setBorder(false);
		panel.setLayout(new AccordionLayout(true));
		SimplePanel gwtPanel = new SimplePanel();
		gwtPanel.setWidget(panel);

		return gwtPanel;
	}

	private Toolbar createToolBar() {
		Toolbar toolbar = new Toolbar();

		ToolbarButton newRuleButton = new ToolbarButton("New rule");
		newRuleButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				controller.newRule();
			}
		});
		toolbar.addButton(newRuleButton);

		return toolbar;
	}

}

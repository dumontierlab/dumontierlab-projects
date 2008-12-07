package com.dumontierlab.ontocreator.ui.client.view;

import java.util.HashMap;
import java.util.Map;

import com.dumontierlab.ontocreator.ui.client.RulesEditor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.AccordionLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

public class RulesEditorView extends Composite {

	private final RulesEditor controller;
	private Panel panel;
	private final Map<String, Label> rules;

	public RulesEditorView(RulesEditor controller) {
		rules = new HashMap<String, Label>();
		this.controller = controller;
		initWidget(createUi());
	}

	public void addRule(final String ruleName) {
		Label ruleLabel = new Label();
		rules.put(ruleName, ruleLabel);

		Panel rulePanel = new Panel(ruleName);
		rulePanel.setHeight(100);
		rulePanel.add(ruleLabel);
		rulePanel.addListener(new PanelListenerAdapter() {
			@Override
			public void onExpand(Panel panel) {
				Window.alert("expanded");
				controller.setActiveRule(ruleName);
			}
		});

		panel.add(rulePanel);
		panel.doLayout();
		rulePanel.expand(true);
		controller.setActiveRule(ruleName);
	}

	public void setRuleText(String ruleName, String text) {
		Label ruleLabel = rules.get(ruleName);
		if (ruleLabel != null) {
			ruleLabel.setText(text);
		}
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

		Menu filtersMenu = new Menu();
		ToolbarMenuButton filtersButton = new ToolbarMenuButton("Filters", filtersMenu);

		Item aBoxQueryButton = new Item("ABox Query", new BaseItemListenerAdapter() {
			@Override
			public void onClick(BaseItem item, EventObject e) {
				controller.addABoxQuery();
			}
		});
		filtersMenu.addItem(aBoxQueryButton);

		Item tBoxQueryButton = new Item("TBox Query", new BaseItemListenerAdapter() {
			@Override
			public void onClick(BaseItem item, EventObject e) {
				controller.addTBoxQuery();
			}
		});
		filtersMenu.addItem(tBoxQueryButton);

		filtersMenu.addSeparator();

		Item dataPropertyRegexButton = new Item("DataProperty Regex", new BaseItemListenerAdapter() {
			@Override
			public void onClick(BaseItem item, EventObject e) {
				controller.addDataPropertyRegex();
			}
		});
		filtersMenu.addItem(dataPropertyRegexButton);

		toolbar.addButton(filtersButton);

		Menu modifiersMenu = new Menu();
		ToolbarMenuButton modifiersButton = new ToolbarMenuButton("Modifiers", modifiersMenu);

		Item concatButton = new Item("Concatenate");
		modifiersMenu.addItem(concatButton);

		Item toLowerCaseButton = new Item("ToLowerCase");
		modifiersMenu.addItem(toLowerCaseButton);

		Item toUpperCaseButton = new Item("ToUpperCase");
		modifiersMenu.addItem(toUpperCaseButton);

		Item toCamelCaseButton = new Item("ToCamelCase");
		modifiersMenu.addItem(toCamelCaseButton);

		Item urlEncodeButton = new Item("URL encode");
		modifiersMenu.addItem(urlEncodeButton);

		toolbar.addButton(modifiersButton);

		Menu constructorsMenu = new Menu();
		ToolbarMenuButton constructorsButton = new ToolbarMenuButton("Constructors", constructorsMenu);

		Item classAssertionAxiomButton = new Item("Class Assertion Axiom");
		classAssertionAxiomButton.addListener(new BaseItemListenerAdapter() {
			@Override
			public void onClick(BaseItem item, EventObject e) {
				controller.addClassAssertionAxiom();
			}
		});
		constructorsMenu.addItem(classAssertionAxiomButton);

		Item DataPropertyAssertionAxiomButton = new Item("Data Property Assertion Axiom");
		constructorsMenu.addItem(DataPropertyAssertionAxiomButton);

		Item ObjectPropertyAssertionAxiomButton = new Item("Object Property Assertion Axiom");
		constructorsMenu.addItem(ObjectPropertyAssertionAxiomButton);

		Item inclusionAxiomButton = new Item("Inclusion Axiom");
		constructorsMenu.addItem(inclusionAxiomButton);

		Item equivalenceAxiomButton = new Item("Equivalence Axiom");
		constructorsMenu.addItem(equivalenceAxiomButton);

		toolbar.addButton(constructorsButton);

		ToolbarButton applyButton = new ToolbarButton("Apply");
		applyButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				controller.applyRule();
			}
		});
		toolbar.addButton(applyButton);

		return toolbar;
	}

}

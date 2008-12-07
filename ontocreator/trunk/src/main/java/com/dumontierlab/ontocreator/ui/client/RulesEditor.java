package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.rpc.RuleService;
import com.dumontierlab.ontocreator.ui.client.rpc.RuleServiceAsync;
import com.dumontierlab.ontocreator.ui.client.view.RulesEditorView;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.gwtext.client.widgets.MessageBox;

public class RulesEditor extends Composite {

	private int counter = 1;
	private final RulesEditorView view;
	private final RuleServiceAsync service;
	private String activeRule;

	public RulesEditor() {
		service = RuleService.Util.getInstance();
		view = new RulesEditorView(this);
		initWidget(view);
	}

	public void setActiveRule(String ruleName) {
		activeRule = ruleName;
	}

	public void newRule() {
		final String ruleName = "Rule " + counter++;
		service.createRule(ruleName, new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				UserMessage.serverError(caught.getMessage(), caught);
				counter--;
			}

			public void onSuccess(Void result) {
				view.addRule(ruleName);
			};
		});

	}

	public void addABoxQuery() {
		MessageBox.prompt("ABox Query Filter", "Enter a class expression using the Manchester OWL Syntax",
				new MessageBox.PromptCallback() {
					public void execute(String btnID, String text) {
						if (MessageBox.OK.getID().equalsIgnoreCase(btnID)) {
							final String currentActiveRule = new String(activeRule);
							service.addABoxQueryFilter(currentActiveRule, text, new AsyncCallback<String>() {
								public void onFailure(Throwable caught) {
									UserMessage.serverError(caught.getMessage(), caught);
								}

								public void onSuccess(String result) {
									view.setRuleText(currentActiveRule, result);
								}
							});
						}
					}
				}, true);

	}

	public void applyRule() {
		final String currentActiveRule = new String(activeRule);
		service.apply(currentActiveRule, new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				UserMessage.serverError(caught.getMessage(), caught);
			}

			public void onSuccess(Void result) {
				MessageBox.alert("Rules Applied", "All rules were applied successfully.");
			}
		});
	}

	public void addClassAssertionAxiom() {
		MessageBox.prompt("Class Assertion Axiom", "Enter a class expression using the Manchester OWL Syntax",
				new MessageBox.PromptCallback() {
					public void execute(String btnID, String text) {
						if (MessageBox.OK.getID().equalsIgnoreCase(btnID)) {
							final String currentActiveRule = new String(activeRule);
							service.addClassAssertion(currentActiveRule, text, new AsyncCallback<String>() {
								public void onFailure(Throwable caught) {
									UserMessage.serverError(caught.getMessage(), caught);
								}

								public void onSuccess(String result) {
									view.setRuleText(currentActiveRule, result);
								}
							});
						}
					}
				}, true);
	}
}

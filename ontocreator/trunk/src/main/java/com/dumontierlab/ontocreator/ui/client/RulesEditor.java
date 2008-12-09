package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.rpc.RuleService;
import com.dumontierlab.ontocreator.ui.client.rpc.RuleServiceAsync;
import com.dumontierlab.ontocreator.ui.client.view.DataPropertyRegexParametersDialog;
import com.dumontierlab.ontocreator.ui.client.view.RulesEditorView;
import com.dumontierlab.ontocreator.ui.client.view.TBoxQueryParametersDialog;
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
		service.createInstanceMapping(ruleName, new AsyncCallback<Void>() {
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
		if (checkIfRuleExists()) {
			final String currentActiveRule = new String(activeRule);
			MessageBox.prompt("ABox Query Filter", "Enter a class expression using the Manchester OWL Syntax",
					new MessageBox.PromptCallback() {
						public void execute(String btnID, String text) {
							if (MessageBox.OK.getID().equalsIgnoreCase(btnID)) {
								service.addABoxQueryFilter(currentActiveRule, text, new UpdateRuleCallback(
										currentActiveRule));
							}
						}
					}, true);
		}

	}

	public void applyRule() {
		if (checkIfRuleExists()) {
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
	}

	public void addClassAssertionAxiom() {
		if (checkIfRuleExists()) {
			final String currentActiveRule = new String(activeRule);
			MessageBox.prompt("Class Assertion Axiom", "Enter a class expression using the Manchester OWL Syntax",
					new MessageBox.PromptCallback() {
						public void execute(String btnID, String text) {
							if (MessageBox.OK.getID().equalsIgnoreCase(btnID)) {
								service.addClassAssertion(currentActiveRule, text, new UpdateRuleCallback(
										currentActiveRule));
							}
						}
					}, true);
		}
	}

	public void addTBoxQuery() {
		if (checkIfRuleExists()) {
			final String currentActiveRule = new String(activeRule);
			final TBoxQueryParametersDialog dialog = new TBoxQueryParametersDialog(
					new TBoxQueryParametersDialog.Listener() {
						public void onComplete(String queryType, String query) {
							service.addTBoxQueryFilter(currentActiveRule, queryType, query, new UpdateRuleCallback(
									currentActiveRule));
						}
					});
			dialog.show();
		}
	}

	public void addDataPropertyRegex() {
		if (checkIfRuleExists()) {
			final String currentActiveRule = new String(activeRule);
			final DataPropertyRegexParametersDialog dialog = new DataPropertyRegexParametersDialog(
					new DataPropertyRegexParametersDialog.Listener() {
						public void onComplete(String property, String regex) {
							service.addDataPropertyRegex(currentActiveRule, property, regex, new UpdateRuleCallback(
									currentActiveRule));
						}
					});
			dialog.show();
		}
	}

	private boolean checkIfRuleExists() {
		if (activeRule == null) {
			MessageBox.alert("First you need to create a new rule.");
			return false;
		}
		return true;
	}

	private class UpdateRuleCallback implements AsyncCallback<String> {
		private final String rule;

		public UpdateRuleCallback(String rule) {
			this.rule = rule;
		}

		public void onFailure(Throwable caught) {
			UserMessage.serverError(caught.getMessage(), caught);
		}

		public void onSuccess(String result) {
			view.setRuleText(rule, result);
		}
	}

}

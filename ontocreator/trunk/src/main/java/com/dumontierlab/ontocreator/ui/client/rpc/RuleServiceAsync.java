package com.dumontierlab.ontocreator.ui.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RuleServiceAsync {

	void createRule(String name, AsyncCallback<Void> callback);

	void addABoxQueryFilter(String ruleName, String query, AsyncCallback<String> callback);
}

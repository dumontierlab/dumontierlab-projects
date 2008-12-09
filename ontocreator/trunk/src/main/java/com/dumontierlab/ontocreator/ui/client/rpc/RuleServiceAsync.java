package com.dumontierlab.ontocreator.ui.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RuleServiceAsync {

	void createInstanceMapping(String name, AsyncCallback<Void> callback);

	void createClassMapping(String name, AsyncCallback<Void> callback);

	void createBoundMapping(String name, String uri, AsyncCallback<Void> callback);

	void addABoxQueryFilter(String ruleName, String query, AsyncCallback<String> callback);

	void addTBoxQueryFilter(String ruleName, String queryType, String query, AsyncCallback<String> callback);

	void addDataPropertyRegex(String ruleName, String propertyUri, String regex, AsyncCallback<String> callback);

	void addClassAssertion(String ruleName, String description, AsyncCallback<String> callback);

	void apply(String ruleName, AsyncCallback<Void> callback);
}

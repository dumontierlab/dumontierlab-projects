package com.dumontierlab.ontocreator.ui.client.rpc;

import com.dumontierlab.ontocreator.ui.client.rpc.exception.NoOutputOntologyException;
import com.dumontierlab.ontocreator.ui.client.rpc.exception.RuleServiceException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("RuleService")
public interface RuleService extends RemoteService {

	void createRule(String name) throws NoOutputOntologyException, RuleServiceException;

	String addABoxQueryFilter(String ruleName, String query) throws NoOutputOntologyException, RuleServiceException;

	class Util {
		private static RuleServiceAsync instance;

		public static RuleServiceAsync getInstance() {
			if (instance == null) {
				instance = (RuleServiceAsync) GWT.create(RuleService.class);
			}
			return instance;
		}
	}
}

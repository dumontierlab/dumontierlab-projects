package com.dumontierlab.ontocreator.ui.client.rpc;

import com.dumontierlab.ontocreator.ui.client.rpc.exception.ServiceException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("RuleService")
public interface RuleService extends RemoteService {

	String createInstanceMapping() throws ServiceException;

	String createClassMapping() throws ServiceException;

	String createBoundMapping(String uri) throws ServiceException;

	String addABoxQueryFilter(String ruleName, String query) throws ServiceException;

	String addTBoxQueryFilter(String ruleName, String queryType, String query) throws ServiceException;

	String addDataPropertyRegex(String ruleName, String propertyUri, String regex) throws ServiceException;

	String addClassAssertion(String ruleName, String description) throws ServiceException;

	void apply(String ruleName) throws ServiceException;

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

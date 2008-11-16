package com.dumontierlab.ontocreator.ui.client.rpc;

import java.util.Set;

import com.dumontierlab.ontocreator.ui.client.model.OWLClassBean;
import com.dumontierlab.ontocreator.ui.client.model.TreeNode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("OntologyService")
public interface OntologyService extends RemoteService {

	Set<String> getLoadedOntologies();

	TreeNode<OWLClassBean> getClassHierarchy();

	class Util {

		private static OntologyServiceAsync instance;

		public static OntologyServiceAsync getInstace() {
			if (instance == null) {
				instance = (OntologyServiceAsync) GWT.create(OntologyService.class);
			}
			return instance;
		}

	}
}

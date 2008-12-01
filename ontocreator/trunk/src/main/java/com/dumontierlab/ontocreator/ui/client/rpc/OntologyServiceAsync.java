package com.dumontierlab.ontocreator.ui.client.rpc;

import java.util.List;
import java.util.Set;

import com.dumontierlab.ontocreator.ui.client.model.OWLClassBean;
import com.dumontierlab.ontocreator.ui.client.model.OWLIndividualBean;
import com.dumontierlab.ontocreator.ui.client.model.OWLPropertyBean;
import com.dumontierlab.ontocreator.ui.client.model.TreeNode;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OntologyServiceAsync {

	void getLoadedOntologies(AsyncCallback<Set<String>> callback);

	void getClassHierarchy(AsyncCallback<TreeNode<OWLClassBean>> callback);

	void getPropertyHierarchy(AsyncCallback<TreeNode<OWLPropertyBean>> callback);

	void getIndividuals(AsyncCallback<List<OWLIndividualBean>> callback);

}

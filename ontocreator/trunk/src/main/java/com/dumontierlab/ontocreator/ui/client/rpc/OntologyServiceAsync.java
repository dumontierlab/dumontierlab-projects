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

	void createOutputOntology(String uri, AsyncCallback<Void> callback);

	void getOutputOntology(AsyncCallback<String> callback);

	void getInputClassHierarchy(AsyncCallback<TreeNode<OWLClassBean>> callback);

	void getInputPropertyHierarchy(AsyncCallback<TreeNode<OWLPropertyBean>> callback);

	void getInputIndividuals(AsyncCallback<List<OWLIndividualBean>> callback);

	void getOutputClassHierarchy(AsyncCallback<TreeNode<OWLClassBean>> callback);

	void getOutputPropertyHierarchy(AsyncCallback<TreeNode<OWLPropertyBean>> callback);

	void getOutputIndividuals(AsyncCallback<List<OWLIndividualBean>> callback);

	void getInputObjectProperties(AsyncCallback<List<OWLPropertyBean>> callback);

	void getInputDataProperties(AsyncCallback<List<OWLPropertyBean>> callback);

}

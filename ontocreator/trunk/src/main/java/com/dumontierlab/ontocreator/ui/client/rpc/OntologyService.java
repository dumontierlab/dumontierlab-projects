package com.dumontierlab.ontocreator.ui.client.rpc;

import java.util.List;
import java.util.Set;

import com.dumontierlab.ontocreator.ui.client.model.OWLClassBean;
import com.dumontierlab.ontocreator.ui.client.model.OWLIndividualBean;
import com.dumontierlab.ontocreator.ui.client.model.OWLPropertyBean;
import com.dumontierlab.ontocreator.ui.client.model.TreeNode;
import com.dumontierlab.ontocreator.ui.client.rpc.exception.ServiceException;
import com.dumontierlab.ontocreator.ui.client.util.RetryException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("OntologyService")
public interface OntologyService extends RemoteService {

	void loadOntology(String physicalUri) throws ServiceException;

	Set<String> getLoadedOntologies() throws RetryException;

	void createOutputOntology(String uri);

	String getOutputOntology() throws RetryException;

	TreeNode<OWLClassBean> getInputClassHierarchy();

	TreeNode<OWLPropertyBean> getInputPropertyHierarchy();

	List<OWLIndividualBean> getInputIndividuals();

	TreeNode<OWLClassBean> getOutputClassHierarchy();

	TreeNode<OWLPropertyBean> getOutputPropertyHierarchy();

	List<OWLIndividualBean> getOutputIndividuals();

	List<OWLPropertyBean> getInputObjectProperties();

	List<OWLPropertyBean> getInputDataProperties();

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

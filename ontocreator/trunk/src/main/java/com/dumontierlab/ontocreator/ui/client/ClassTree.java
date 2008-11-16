package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.model.OWLClassBean;
import com.dumontierlab.ontocreator.ui.client.model.TreeNode;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyServiceAsync;
import com.dumontierlab.ontocreator.ui.client.view.ClassTreeView;
import com.google.gwt.user.client.ui.Composite;

public class ClassTree extends Composite {

	private final ClassTreeView view;
	private final OntologyServiceAsync service;

	public ClassTree() {
		service = OntologyService.Util.getInstace();
		view = new ClassTreeView(this);
		initWidget(view);
	}

	public void setTreeModel(TreeNode<OWLClassBean> root) {
		view.setModel(root);
	}
}

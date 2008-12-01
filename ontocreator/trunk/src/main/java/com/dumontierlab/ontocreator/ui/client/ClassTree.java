package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.model.OWLClassBean;
import com.dumontierlab.ontocreator.ui.client.model.TreeNode;
import com.dumontierlab.ontocreator.ui.client.view.ClassTreeView;
import com.google.gwt.user.client.ui.Composite;

public class ClassTree extends Composite {

	private final ClassTreeView view;

	public ClassTree() {
		view = new ClassTreeView();
		initWidget(view);
	}

	public void setTreeModel(TreeNode<OWLClassBean> root) {
		view.setModel(root);
	}
}

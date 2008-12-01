package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.model.OWLPropertyBean;
import com.dumontierlab.ontocreator.ui.client.model.TreeNode;
import com.dumontierlab.ontocreator.ui.client.view.PropertyTreeView;
import com.google.gwt.user.client.ui.Composite;

public class PropertyTree extends Composite {

	private final PropertyTreeView view;

	public PropertyTree() {
		view = new PropertyTreeView();
		initWidget(view);
	}

	public void setTreeModel(TreeNode<OWLPropertyBean> root) {
		view.setModel(root);
	}

}

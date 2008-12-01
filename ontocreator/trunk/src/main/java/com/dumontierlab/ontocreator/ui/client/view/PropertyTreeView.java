package com.dumontierlab.ontocreator.ui.client.view;

import com.dumontierlab.ontocreator.ui.client.model.OWLPropertyBean;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;

public class PropertyTreeView extends Composite {

	private TreeNode root;

	public PropertyTreeView() {
		initWidget(createUi());
	}

	private Widget createUi() {
		TreePanel tree = new TreePanel();
		root = new TreeNode();
		tree.setRootNode(root);
		tree.setRootVisible(false);

		SimplePanel panel = new SimplePanel();
		panel.setWidget(tree);
		return panel;
	}

	public void setModel(com.dumontierlab.ontocreator.ui.client.model.TreeNode<OWLPropertyBean> model) {
		for (Node node : root.getChildNodes()) {
			node.remove();
		}
		for (com.dumontierlab.ontocreator.ui.client.model.TreeNode<OWLPropertyBean> modelNode : model) {
			root.appendChild(createTreeNode(modelNode));
		}
		root.expand();
	}

	private TreeNode createTreeNode(com.dumontierlab.ontocreator.ui.client.model.TreeNode<OWLPropertyBean> model) {
		OWLPropertyBean propertyBean = model.getValue();
		TreeNode root = new TreeNode(propertyBean.getLabel());
		for (com.dumontierlab.ontocreator.ui.client.model.TreeNode<OWLPropertyBean> modelNode : model) {
			TreeNode node = createTreeNode(modelNode);
			root.appendChild(node);
		}
		return root;
	}

}

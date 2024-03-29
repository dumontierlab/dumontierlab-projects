package com.dumontierlab.ontocreator.ui.client.view;

import com.dumontierlab.ontocreator.ui.client.model.OWLClassBean;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;

public class ClassTreeView extends Composite {

	private static final String UNSATISFIABLE_STYLE_CLASS = null;

	private TreeNode owlThing;

	public ClassTreeView() {
		initWidget(createUi());
	}

	private Widget createUi() {
		TreePanel tree = new TreePanel();
		owlThing = new TreeNode("Thing");
		tree.setRootNode(owlThing);

		SimplePanel panel = new SimplePanel();
		panel.setWidget(tree);
		return panel;
	}

	public void setModel(com.dumontierlab.ontocreator.ui.client.model.TreeNode<OWLClassBean> model) {
		for (Node node : owlThing.getChildNodes()) {
			node.remove();
		}
		for (com.dumontierlab.ontocreator.ui.client.model.TreeNode<OWLClassBean> modelNode : model) {
			owlThing.appendChild(createTreeNode(modelNode));
		}
		owlThing.expand();
	}

	private TreeNode createTreeNode(com.dumontierlab.ontocreator.ui.client.model.TreeNode<OWLClassBean> model) {
		OWLClassBean classBean = model.getValue();
		TreeNode root = new TreeNode(classBean.getLabel());
		if (classBean.isUnsatisfiable()) {
			root.setCls(UNSATISFIABLE_STYLE_CLASS);
		}
		for (com.dumontierlab.ontocreator.ui.client.model.TreeNode<OWLClassBean> modelNode : model) {
			TreeNode node = createTreeNode(modelNode);
			root.appendChild(node);
		}
		return root;
	}

}

package com.dumontierlab.ontocreator.ui.client.view;

import com.dumontierlab.ontocreator.ui.client.ClassTree;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;

public class ClassTreeView extends Composite {

	private final ClassTree controller;
	private TreePanel tree;

	public ClassTreeView(ClassTree controller) {
		this.controller = controller;
		initWidget(createUi());
	}

	private Widget createUi() {
		tree = new TreePanel();
		tree.setRootNode(new TreeNode("owl:Thing"));
		return tree;
	}

}

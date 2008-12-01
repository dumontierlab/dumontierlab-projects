package com.dumontierlab.ontocreator.ui.client.view;

import java.util.List;

import com.dumontierlab.ontocreator.ui.client.model.OWLIndividualBean;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;

public class IndividualListView extends Composite {

	private TreeNode root;

	public IndividualListView() {
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

	public void setModel(List<OWLIndividualBean> model) {
		for (OWLIndividualBean individual : model) {
			root.appendChild(createTreeNode(individual));
		}
		root.expand();
	}

	private TreeNode createTreeNode(OWLIndividualBean individual) {
		TreeNode root = new TreeNode(individual.getLabel());
		return root;
	}

}

package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.view.ClassTreeView;
import com.google.gwt.user.client.ui.Composite;

public class ClassTree extends Composite {

	private final ClassTreeView view;

	public ClassTree() {
		view = new ClassTreeView(this);
		initWidget(view);
	}
}

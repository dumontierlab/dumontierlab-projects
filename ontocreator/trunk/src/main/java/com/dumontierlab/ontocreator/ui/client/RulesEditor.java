package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.view.RulesEditorView;
import com.google.gwt.user.client.ui.Composite;

public class RulesEditor extends Composite {

	private final RulesEditorView view;

	public RulesEditor() {
		view = new RulesEditorView(this);
		initWidget(view);
	}

}

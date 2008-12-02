package com.dumontierlab.ontocreator.ui.client.test;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class TestEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		final Panel p = new Panel();
		p.setLayout(new VerticalLayout());
		p.setHeight(600);

		final Panel rules = new Panel();
		rules.setHeight(400);

		rules.add(new Label("a"));

		p.add(rules);
		p.add(new Button("click", new ClickListener() {
			public void onClick(Widget sender) {
				Window.alert("aleret");
				Panel rule = new Panel("assas");
				rule.add(new Label("DSadasdasads"));
				rules.add(rule);
				rules.doLayout();
			}
		}));
		new Viewport(p);

	}
}

package com.dumontierlab.ontocreator.ui.client.view;

import com.dumontierlab.ontocreator.ui.client.OntoCreatorMenu;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

public class OntoCreatorMenuView extends Composite {

	private final OntoCreatorMenu controller;

	public OntoCreatorMenuView(OntoCreatorMenu _controller) {
		controller = _controller;
		initWidget(createUi());
	}

	private Widget createUi() {
		Toolbar toolbar = new Toolbar();
		toolbar.addButton(createFileMenu());
		return toolbar;
	}

	private ToolbarMenuButton createFileMenu() {
		Menu fileMenu = new Menu();

		Item loadTabFileItem = new Item("Tab File", new BaseItemListenerAdapter() {
			@Override
			public void onClick(BaseItem item, EventObject e) {
				controller.loadTabFile();
			}
		});

		Item loadOntologyItem = new Item("Ontology", new BaseItemListenerAdapter() {
			@Override
			public void onClick(BaseItem item, EventObject e) {
				controller.loadOntology();

			}
		});

		Menu loadMenu = new Menu();
		loadMenu.addItem(loadTabFileItem);
		loadMenu.addItem(loadOntologyItem);

		MenuItem loadSubmenu = new MenuItem("Load", loadMenu);

		fileMenu.addItem(loadSubmenu);

		return new ToolbarMenuButton("File", fileMenu);
	}
}

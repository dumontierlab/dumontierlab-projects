package com.dumontierlab.ontocreator.ui.client.view;

import com.dumontierlab.ontocreator.ui.client.OntoCreatorMenu;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class OntoCreatorMenuView extends MenuBar {

	private final OntoCreatorMenu controller;

	public OntoCreatorMenuView(OntoCreatorMenu _controller) {
		controller = _controller;
		MenuItem fileItem = new MenuItem("File", createFileMenu());
		addItem(fileItem);

	}

	private MenuBar createFileMenu() {
		MenuBar fileBar = new MenuBar();
		MenuBar loadBar = new MenuBar();
		MenuItem loadTabFileItem = new MenuItem("Tab File", new Command() {
			public void execute() {
				controller.loadTabFile();

			}
		});
		MenuItem loadOntologyItem = new MenuItem("Ontology", new Command() {
			public void execute() {
				controller.loadOntology();

			}
		});
		loadBar.addItem(loadTabFileItem);
		loadBar.addItem(loadOntologyItem);
		MenuItem loadItem = new MenuItem("Load", loadBar);
		fileBar.addItem(loadItem);
		return fileBar;
	}

}

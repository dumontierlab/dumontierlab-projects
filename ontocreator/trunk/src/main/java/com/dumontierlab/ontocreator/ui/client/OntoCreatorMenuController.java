package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.util.Constants;

public class OntoCreatorMenuController {

	private final OntoCreatorMenu menu;

	public OntoCreatorMenuController() {
		menu = new OntoCreatorMenu(this);
	}

	public OntoCreatorMenu getMenu() {
		return menu;
	}

	public void loadTabFile() {
		FileChooserDialog chooser = new FileChooserDialog("Select a tab file",
				Constants.TAB_FILE_TYPE);
		chooser.show();
	}

	public void loadOntology() {
		FileChooserDialog chooser = new FileChooserDialog(
				"Select an OWL ontology file", Constants.ONTOLOGY_FILE_TYPE);
		chooser.show();
	}

}

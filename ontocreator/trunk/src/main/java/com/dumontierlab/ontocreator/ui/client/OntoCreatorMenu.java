package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.util.Constants;
import com.dumontierlab.ontocreator.ui.client.view.OntoCreatorMenuView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;

public class OntoCreatorMenu extends Composite {

	private final OntoCreatorMenuView view;

	public OntoCreatorMenu() {
		view = new OntoCreatorMenuView(this);
		initWidget(view);
	}

	public OntoCreatorMenuView getView() {
		return view;
	}

	public void loadOntology() {
		FileUploadDialog chooser = new FileUploadDialog("Select an OWL ontology file", Constants.ONTOLOGY_FILE_TYPE);
		chooser.show();
	}

	public void saveOutput() {
		Window.open(GWT.getModuleBaseURL() + "download", "Output Ontology", null);
	}

}

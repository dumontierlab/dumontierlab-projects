package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.dumontierlab.ontocreator.ui.client.util.Constants;
import com.dumontierlab.ontocreator.ui.client.view.OntoCreatorMenuView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.gwtext.client.widgets.MessageBox;

public class OntoCreatorMenu extends Composite {

	private final OntoCreatorMenuView view;

	public OntoCreatorMenu() {
		view = new OntoCreatorMenuView(this);
		initWidget(view);
	}

	public OntoCreatorMenuView getView() {
		return view;
	}

	public void loadTabFile() {
		FileUploadDialog chooser = new FileUploadDialog("Select a tab file", Constants.TAB_FILE_TYPE);
		chooser.show();
	}

	public void loadOntology() {
		FileUploadDialog chooser = new FileUploadDialog("Select an OWL ontology file", Constants.ONTOLOGY_FILE_TYPE);
		chooser.show();
	}

	public void newOutputOntology() {
		MessageBox.prompt("New Output Ontology", "Ontology URI: ", new MessageBox.PromptCallback() {
			public void execute(String btnID, final String text) {
				OntologyService.Util.getInstace().createOutputOntology(text, new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						UserMessage.serverError("Unable to create output ontology with URI: " + text, caught);
					}

					public void onSuccess(Void result) {
						// nothing to do
					}
				});
			}
		});
	}

	public void saveOutput() {
		Window.open(GWT.getModuleBaseURL() + "download", "Output Ontology", null);
	}

}

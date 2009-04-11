package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.dumontierlab.ontocreator.ui.client.util.Constants;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TabFileWizardInputs extends Composite implements WizardTab {

	private static final String URI_TEXTBOX_STYLE_NAME = "uriTextbox";

	private FileUploadWidget fileUpload;
	private TextBox uriTextBox;

	public TabFileWizardInputs() {
		initWidget(createUi());
	}

	public String getTabCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTabName() {
		return "Step 1: Choose a tab file";
	}

	@Override
	public Widget getWidget() {
		return this;
	}

	public void initialize() {
		// empty
	}

	public void complete(final AsyncCallback<Boolean> callback) {
		if (fileUpload.getFileName() != null && fileUpload.getFileName().length() != 0 && uriTextBox.getText() != null
				&& uriTextBox.getText().length() != 0) {
			fileUpload.setFileUploadListener(new FileUploadWidget.FileUploadListener() {
				public void onFileUpload() {
					OntologyService.Util.getInstace().createOutputOntology(uriTextBox.getText(),
							new AsyncCallback<Void>() {
								public void onFailure(Throwable caught) {
									callback.onFailure(caught);
								}

								public void onSuccess(Void result) {
									callback.onSuccess(true);
								}
							});
				}
			});
			fileUpload.upload();
		} else {
			callback.onSuccess(false);
		}

	}

	private Widget createUi() {
		FlowPanel panel = new FlowPanel();

		HorizontalPanel tabFilePanel = new HorizontalPanel();
		tabFilePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		tabFilePanel.setSpacing(5);

		tabFilePanel.add(new Label("Tab file:"));
		fileUpload = new FileUploadWidget(Constants.TAB_FILE_TYPE);
		tabFilePanel.add(fileUpload);

		HorizontalPanel outputUriPanel = new HorizontalPanel();
		outputUriPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		outputUriPanel.setSpacing(5);

		outputUriPanel.add(new Label("Output ontology's URI:"));
		uriTextBox = new TextBox();
		uriTextBox.addStyleName(URI_TEXTBOX_STYLE_NAME);
		outputUriPanel.add(uriTextBox);

		panel.add(tabFilePanel);
		panel.add(outputUriPanel);

		return panel;
	}
}

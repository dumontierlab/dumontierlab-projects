package com.dumontierlab.ontocreator.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FileChooserDialog extends DialogBox {

	private final String type;

	public FileChooserDialog(String dialogTitle, String _type) {
		type = _type;
		setText(dialogTitle);
		setWidget(createUi());
	}

	private Widget createUi() {
		final FormPanel form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.setAction(GWT.getModuleBaseURL() + "fileUpload");

		VerticalPanel panel = new VerticalPanel();

		// panel.add(new Hidden(Constants.FILE_TYPE_PARAMETER, type));

		FileUpload fileUpload = new FileUpload();
		panel.add(fileUpload);

		HorizontalPanel buttonsPanel = new HorizontalPanel();

		Button acceptButton = new Button("Accept", new ClickListener() {
			public void onClick(Widget sender) {
				form.submit();
			}
		});
		Button cancelButton = new Button("Cancel", new ClickListener() {
			public void onClick(Widget sender) {
				hide();
			};
		});

		buttonsPanel.add(cancelButton);
		buttonsPanel.setCellWidth(cancelButton, "100%");
		buttonsPanel.setCellHorizontalAlignment(cancelButton,
				HorizontalPanel.ALIGN_RIGHT);
		buttonsPanel.add(new Spacer("8px", null));
		buttonsPanel.add(acceptButton);
		buttonsPanel.setCellHorizontalAlignment(acceptButton,
				HorizontalPanel.ALIGN_RIGHT);

		buttonsPanel.setWidth("100%");
		panel.add(new Spacer(null, "10px"));
		panel.add(buttonsPanel);
		form.add(panel);
		return form;
	}
}

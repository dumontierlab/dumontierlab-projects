package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.util.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
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
		VerticalPanel container = new VerticalPanel();

		final FormPanel form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.setAction(GWT.getModuleBaseURL() + "fileUpload?"
				+ Constants.FILE_TYPE_PARAMETER + "=" + URL.encode(type));

		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		FileUpload fileUpload = new FileUpload();
		fileUpload.setName("upload-file");
		panel.add(fileUpload);

		panel.add(new Spacer(null, "10px"));
		container.add(form);

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
		container.add(buttonsPanel);

		return container;
	}
}

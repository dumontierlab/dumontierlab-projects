package com.dumontierlab.ontocreator.ui.client;

import com.dumontierlab.ontocreator.ui.client.util.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FileUploadWidget extends Composite {

	private final FormPanel form;
	private final FileUpload fileUpload;
	private final String type;

	public FileUploadWidget(String type) {
		this.type = type;
		form = new FormPanel();
		fileUpload = new FileUpload();
		initWidget(createUi());
	}

	public String getFileName() {
		return fileUpload.getFilename();
	}

	public void upload() {
		form.submit();
	}

	public void addFileUploadListener(final FileUploadListener listener) {
		FormHandler handler = new FormHandler() {
			public void onSubmit(FormSubmitEvent event) {
				// empty;
			}

			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				listener.onFileUpload();
			};
		};
		form.addFormHandler(handler);
	}

	public void clear() {
		form.clear();
	}

	private Widget createUi() {
		VerticalPanel container = new VerticalPanel();

		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.setAction(GWT.getModuleBaseURL() + "upload?" + Constants.FILE_TYPE_PARAMETER + "=" + URL.encode(type));

		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		fileUpload.setName("upload-file");
		panel.add(fileUpload);

		panel.add(new Spacer(null, "10px"));
		container.add(form);

		return container;
	}

	public interface FileUploadListener {
		void onFileUpload();
	}
}

package com.dumontierlab.ontocreator.ui.client;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.WaitConfig;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

public class FileUploadDialog extends Window {

	private final FileUploadWidget fileUpload;
	private MessageBoxConfig waitConfig;

	public FileUploadDialog(String title, String type) {
		super(title, true, false);
		fileUpload = new FileUploadWidget(type);
		fileUpload.setFileUploadListener(new FileUploadWidget.FileUploadListener() {
			public void onFileUpload() {
				close();
			}
		});
		createUi();
		setClosable(true);
		setPaddings(8);
		setWidth(220);
		setHeight(100);
		setCloseAction(Window.CLOSE);
	}

	private void createUi() {
		setLayout(new FitLayout());
		add(fileUpload);
		addButton(new Button("Upload", new ButtonListenerAdapter() {
			@Override
			public void onClick(final Button button, EventObject e) {
				String fileName = fileUpload.getFileName();
				if (fileName == null || fileName.length() == 0) {
					return;
				}
				// createWaitCongig(button);
				fileUpload.upload();
			}
		}));
		addButton(new Button("Cancel", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				close();
			}
		}));
		setButtonAlign(Position.CENTER);
	}

	private FileUploadDialog self() {
		return this;
	}

	private void createWaitCongig(final Button button) {
		MessageBoxConfig waitConfig = new MessageBoxConfig() {
			{
				setMsg("Saving your data, please wait...");
				setProgressText("Saving...");
				setWidth(300);
				setWait(true);
				setWaitConfig(new WaitConfig() {
					{
						setInterval(200);
					}
				});
				setAnimEl(button.getId());
			}
		};
	}

}

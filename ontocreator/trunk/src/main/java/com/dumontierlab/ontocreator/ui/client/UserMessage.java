package com.dumontierlab.ontocreator.ui.client;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.widgets.MessageBox;

public class UserMessage {

	public static void serverError(String message, Throwable caught) {
		MessageBox.alert(message);
		GWT.log(message, caught);
	}

}

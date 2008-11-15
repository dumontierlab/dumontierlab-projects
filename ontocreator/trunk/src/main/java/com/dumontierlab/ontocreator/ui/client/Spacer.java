package com.dumontierlab.ontocreator.ui.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class Spacer extends Widget {

	public Spacer() {
		this(null, null);
	}

	public Spacer(String width, String height) {
		setElement(DOM.createDiv());
		if (width != null) {
			setWidth(width);
		}
		if (height != null) {
			setHeight(height);
		}
	}

}

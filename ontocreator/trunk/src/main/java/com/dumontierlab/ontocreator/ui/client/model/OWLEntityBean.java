package com.dumontierlab.ontocreator.ui.client.model;

import java.io.Serializable;

public class OWLEntityBean implements Serializable {

	private String uri;
	private String label;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}

package com.dumontierlab.ontocreator.ui.client.model;

import java.io.Serializable;

public class OWLClassBean implements Serializable {

	private String uri;
	private String label;
	private boolean unsatisfiable;

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

	public boolean isUnsatisfiable() {
		return unsatisfiable;
	}

	public void setUnsatisfiable(boolean unsatisfiable) {
		this.unsatisfiable = unsatisfiable;
	}

}

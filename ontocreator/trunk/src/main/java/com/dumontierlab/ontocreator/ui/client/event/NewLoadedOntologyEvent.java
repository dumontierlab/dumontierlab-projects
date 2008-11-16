package com.dumontierlab.ontocreator.ui.client.event;

public class NewLoadedOntologyEvent implements UiEvent {

	public static final String EVENT_NAME = "NewLoadedOntologyEvent";

	private final String ontologyUri;

	public NewLoadedOntologyEvent(String ontologyUri) {
		this.ontologyUri = ontologyUri;
	}

	public String getOntologyUri() {
		return ontologyUri;
	}

	public String getEventName() {
		return EVENT_NAME;
	}

}

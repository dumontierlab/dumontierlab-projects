package com.dumontierlab.ontocreator.ui.client.event;

public class InputOntologiesChangedEvent implements UiEvent {

	public static final String EVENT_NAME = "InputOntologiesChangedEvent";

	private final String[] ontologyUris;

	public InputOntologiesChangedEvent(String[] ontologies) {
		ontologyUris = ontologies;
	}

	public String getEventName() {
		return EVENT_NAME;
	}

	public String[] getOntologyUris() {
		return ontologyUris;
	}

}

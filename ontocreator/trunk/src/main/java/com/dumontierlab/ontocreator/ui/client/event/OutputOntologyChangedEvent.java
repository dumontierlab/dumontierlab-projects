package com.dumontierlab.ontocreator.ui.client.event;

public class OutputOntologyChangedEvent implements UiEvent {

	public static final String EVENT_NAME = "OutputOntologyChangedEvent";

	private final String outputOntologyUri;

	public OutputOntologyChangedEvent(String outputOntologyUri) {
		this.outputOntologyUri = outputOntologyUri;
	}

	public String getEventName() {
		return EVENT_NAME;
	}

	public String getOutputOntologyUri() {
		return outputOntologyUri;
	}

}

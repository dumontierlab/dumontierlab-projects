package com.dumontierlab.ontocreator.ui.client.event;

import java.util.Set;

public interface UiEventHandler {

	void handleEvent(UiEvent event);

	Set<String> getEventOfInterest();

}

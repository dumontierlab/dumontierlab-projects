package com.dumontierlab.ontocreator.ui.client.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UiEventBroker {

	private static UiEventBroker instance;

	private final Map<String, List<UiEventHandler>> eventHandlers;

	public static UiEventBroker getInstance() {
		if (instance == null) {
			instance = new UiEventBroker();
		}
		return instance;
	}

	private UiEventBroker() {
		eventHandlers = new HashMap<String, List<UiEventHandler>>();
	}

	public void registerEventHandler(UiEventHandler handler) {
		for (String eventName : handler.getEventOfInterest()) {
			List<UiEventHandler> list = eventHandlers.get(eventName);
			if (list == null) {
				list = new ArrayList<UiEventHandler>();
			}
			list.add(handler);
			eventHandlers.put(eventName, list);
		}
	}

	public void removeEventHandler(UiEventHandler handler) {
		for (String eventName : handler.getEventOfInterest()) {
			List<UiEventHandler> list = eventHandlers.get(eventName);
			if (list != null) {
				list.remove(handler);
				if (list.isEmpty()) {
					eventHandlers.remove(eventName);
				}
			}
		}
	}

	public void publish(UiEvent event) {
		if (eventHandlers.containsKey(event.getEventName())) {
			for (UiEventHandler handler : eventHandlers.get(event.getEventName())) {
				handler.handleEvent(event);
			}
		}
	}

}

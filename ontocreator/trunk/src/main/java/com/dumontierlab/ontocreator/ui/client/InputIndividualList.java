package com.dumontierlab.ontocreator.ui.client;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.dumontierlab.ontocreator.ui.client.event.InputOntologiesChangedEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEventBroker;
import com.dumontierlab.ontocreator.ui.client.event.UiEventHandler;
import com.dumontierlab.ontocreator.ui.client.model.OWLIndividualBean;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.dumontierlab.ontocreator.ui.client.util.OnRequestRpcCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class InputIndividualList extends IndividualList implements UiEventHandler {

	private final OnRequestRpcCommand<List<OWLIndividualBean>> rpcCommand;

	public InputIndividualList() {
		rpcCommand = new OnRequestRpcCommand<List<OWLIndividualBean>>() {
			@Override
			protected void rpcCall(AsyncCallback<List<OWLIndividualBean>> callback) {
				OntologyService.Util.getInstace().getInputIndividuals(callback);
			}

			@Override
			protected void rpcFail(Throwable caught) {
				UserMessage.serverError("Unable to get individuals for input ontologies", caught);
			}

			@Override
			protected void rpcReturn(List<OWLIndividualBean> result) {
				setIndividuals(result);
			}
		};
		UiEventBroker.getInstance().registerEventHandler(this);
	}

	public Set<String> getEventOfInterest() {
		return Collections.singleton(InputOntologiesChangedEvent.EVENT_NAME);
	}

	public void handleEvent(UiEvent event) {
		if (event.getEventName().equals(InputOntologiesChangedEvent.EVENT_NAME)) {
			rpcCommand.call();
		}
	}

}

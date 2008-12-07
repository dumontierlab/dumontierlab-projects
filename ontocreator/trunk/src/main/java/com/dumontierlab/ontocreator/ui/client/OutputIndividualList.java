package com.dumontierlab.ontocreator.ui.client;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.dumontierlab.ontocreator.ui.client.event.OutputOntologyChangedEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEventBroker;
import com.dumontierlab.ontocreator.ui.client.event.UiEventHandler;
import com.dumontierlab.ontocreator.ui.client.model.OWLIndividualBean;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.dumontierlab.ontocreator.ui.client.util.OnRequestRpcCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OutputIndividualList extends IndividualList implements UiEventHandler {

	private final OnRequestRpcCommand<List<OWLIndividualBean>> rpcCommand;

	public OutputIndividualList() {
		rpcCommand = new OnRequestRpcCommand<List<OWLIndividualBean>>() {
			@Override
			protected void rpcCall(AsyncCallback<List<OWLIndividualBean>> callback) {
				OntologyService.Util.getInstace().getOutputIndividuals(callback);
			}

			@Override
			protected void rpcFail(Throwable caught) {
				UserMessage.serverError("Unable to get individuals for output ontology", caught);
			}

			@Override
			protected void rpcReturn(List<OWLIndividualBean> result) {
				setIndividuals(result);
			}
		};
		UiEventBroker.getInstance().registerEventHandler(this);
	}

	public Set<String> getEventOfInterest() {
		return Collections.singleton(OutputOntologyChangedEvent.EVENT_NAME);
	}

	public void handleEvent(UiEvent event) {
		if (event.getEventName().equals(OutputOntologyChangedEvent.EVENT_NAME)) {
			rpcCommand.call();
		}
	}

}

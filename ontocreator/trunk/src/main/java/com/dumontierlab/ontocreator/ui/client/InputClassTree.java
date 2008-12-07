package com.dumontierlab.ontocreator.ui.client;

import java.util.Collections;
import java.util.Set;

import com.dumontierlab.ontocreator.ui.client.event.InputOntologiesChangedEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEventBroker;
import com.dumontierlab.ontocreator.ui.client.event.UiEventHandler;
import com.dumontierlab.ontocreator.ui.client.model.OWLClassBean;
import com.dumontierlab.ontocreator.ui.client.model.TreeNode;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.dumontierlab.ontocreator.ui.client.util.OnRequestRpcCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class InputClassTree extends ClassTree implements UiEventHandler {

	private final OnRequestRpcCommand<TreeNode<OWLClassBean>> rpcCommand;

	public InputClassTree() {
		rpcCommand = new OnRequestRpcCommand<TreeNode<OWLClassBean>>() {
			@Override
			protected void rpcCall(AsyncCallback<TreeNode<OWLClassBean>> callback) {
				OntologyService.Util.getInstace().getInputClassHierarchy(callback);
			}

			@Override
			protected void rpcFail(Throwable caught) {
				UserMessage.serverError("Unable to get class hierarchy for input ontologies", caught);
			}

			@Override
			protected void rpcReturn(TreeNode<OWLClassBean> result) {
				setTreeModel(result);
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

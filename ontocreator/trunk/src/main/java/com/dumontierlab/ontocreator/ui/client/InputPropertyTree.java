package com.dumontierlab.ontocreator.ui.client;

import java.util.Collections;
import java.util.Set;

import com.dumontierlab.ontocreator.ui.client.event.InputOntologiesChangedEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEventBroker;
import com.dumontierlab.ontocreator.ui.client.event.UiEventHandler;
import com.dumontierlab.ontocreator.ui.client.model.OWLPropertyBean;
import com.dumontierlab.ontocreator.ui.client.model.TreeNode;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.dumontierlab.ontocreator.ui.client.util.OnRequestRpcCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class InputPropertyTree extends PropertyTree implements UiEventHandler {

	private final OnRequestRpcCommand<TreeNode<OWLPropertyBean>> rpcCommand;

	public InputPropertyTree() {
		rpcCommand = new OnRequestRpcCommand<TreeNode<OWLPropertyBean>>() {
			@Override
			protected void rpcCall(AsyncCallback<TreeNode<OWLPropertyBean>> callback) {
				OntologyService.Util.getInstace().getPropertyHierarchy(callback);
			}

			@Override
			protected void rpcFail(Throwable caught) {
				UserMessage.serverError("Unable to get properties hierarchy for input ontologies", caught);
			}

			@Override
			protected void rpcReturn(TreeNode<OWLPropertyBean> result) {
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

package com.dumontierlab.ontocreator.ui.client;

import java.util.Collections;
import java.util.Set;

import com.dumontierlab.ontocreator.ui.client.event.OutputOntologyChangedEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEventHandler;
import com.dumontierlab.ontocreator.ui.client.model.OWLPropertyBean;
import com.dumontierlab.ontocreator.ui.client.model.TreeNode;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.dumontierlab.ontocreator.ui.client.util.OnRequestRpcCommand;
import com.dumontierlab.ontocreator.ui.client.util.RpcCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OutputPropertyTree extends PropertyTree implements UiEventHandler {

	private final RpcCommand rpcCommand;

	public OutputPropertyTree() {
		rpcCommand = new OnRequestRpcCommand<TreeNode<OWLPropertyBean>>() {
			@Override
			protected void rpcCall(AsyncCallback<TreeNode<OWLPropertyBean>> callback) {
				OntologyService.Util.getInstace().getOutputPropertyHierarchy(callback);
			}

			@Override
			protected void rpcFail(Throwable caught) {
				UserMessage.serverError("Unable to get output properties", caught);
			}

			@Override
			protected void rpcReturn(TreeNode<OWLPropertyBean> result) {
				setTreeModel(result);
			}
		};
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

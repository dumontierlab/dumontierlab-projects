package com.dumontierlab.ontocreator.ui.client;

import java.util.Collections;
import java.util.Set;

import com.dumontierlab.ontocreator.ui.client.event.OutputOntologyChangedEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEventBroker;
import com.dumontierlab.ontocreator.ui.client.event.UiEventHandler;
import com.dumontierlab.ontocreator.ui.client.model.OWLClassBean;
import com.dumontierlab.ontocreator.ui.client.model.TreeNode;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.dumontierlab.ontocreator.ui.client.util.ContinuousRpcCommand;
import com.dumontierlab.ontocreator.ui.client.util.DatedResponse;
import com.dumontierlab.ontocreator.ui.client.util.OnRequestRpcCommand;
import com.dumontierlab.ontocreator.ui.client.util.RpcCommandPool;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OutputClassTree extends ClassTree implements UiEventHandler {

	private final RpcCommandPool rpcPool;
	private OnRequestRpcCommand<TreeNode<OWLClassBean>> rpcCommand;

	public OutputClassTree() {
		rpcPool = new RpcCommandPool();
		initRpc();
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

	@Override
	protected void onLoad() {
		rpcPool.resumeAllRpc();
	}

	@Override
	protected void onUnload() {
		rpcPool.suspendAllRpc();
	}

	private void initRpc() {
		rpcCommand = new OnRequestRpcCommand<TreeNode<OWLClassBean>>() {
			@Override
			protected void rpcCall(AsyncCallback<TreeNode<OWLClassBean>> callback) {
				OntologyService.Util.getInstace().getOutputClassHierarchy(callback);
			}

			@Override
			protected void rpcFail(Throwable caught) {
				UserMessage.serverError("Unable to get class hierarchy for output ontology", caught);
			}

			@Override
			protected void rpcReturn(TreeNode<OWLClassBean> result) {
				setTreeModel(result);
			}
		};

		rpcPool.addRpcCommand(new ContinuousRpcCommand<DatedResponse<String>>() {

			private long lastUpdate;

			@Override
			protected void rpcCall(AsyncCallback<DatedResponse<String>> callback) {
				OntologyService.Util.getInstace().getOutputOntology(lastUpdate, callback);
			}

			@Override
			protected void rpcFail(Throwable caught) {
				UserMessage.serverError("Unable to check output ontology", caught);
			}

			@Override
			protected void rpcReturn(DatedResponse<String> result) {
				lastUpdate = result.getTimestamp();
				String ouputOntologyUri = result.getValue();
				if (result != null) {
					UiEventBroker.getInstance().publish(new OutputOntologyChangedEvent(ouputOntologyUri));
				}
			}
		});
	};

}

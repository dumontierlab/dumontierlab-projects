package com.dumontierlab.ontocreator.ui.client;

import java.util.Collections;
import java.util.Set;

import com.dumontierlab.ontocreator.ui.client.event.NewLoadedOntologyEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEventBroker;
import com.dumontierlab.ontocreator.ui.client.event.UiEventHandler;
import com.dumontierlab.ontocreator.ui.client.model.OWLClassBean;
import com.dumontierlab.ontocreator.ui.client.model.TreeNode;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyServiceAsync;
import com.dumontierlab.ontocreator.ui.client.view.ClassTreeView;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

public class ClassTree extends Composite implements UiEventHandler {

	private final ClassTreeView view;
	private final OntologyServiceAsync service;

	public ClassTree() {
		service = OntologyService.Util.getInstace();
		view = new ClassTreeView(this);
		initWidget(view);
		UiEventBroker.getInstance().registerEventHandler(this);
	}

	public Set<String> getEventOfInterest() {
		return Collections.singleton(NewLoadedOntologyEvent.EVENT_NAME);
	}

	public void handleEvent(UiEvent event) {
		if (event.getEventName().equals(NewLoadedOntologyEvent.EVENT_NAME)) {
			NewLoadedOntologyEvent newLoadedOntologyEvent = (NewLoadedOntologyEvent) event;
			service.getClassHierarchy(new AsyncCallback<TreeNode<OWLClassBean>>() {
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}

				public void onSuccess(TreeNode<OWLClassBean> result) {
					view.setModel(result);
				}

			});
		}
	}
}

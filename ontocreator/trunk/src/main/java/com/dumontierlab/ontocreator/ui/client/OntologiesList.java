package com.dumontierlab.ontocreator.ui.client;

import java.util.Set;

import com.dumontierlab.ontocreator.ui.client.event.InputOntologiesChangedEvent;
import com.dumontierlab.ontocreator.ui.client.event.UiEventBroker;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyServiceAsync;
import com.dumontierlab.ontocreator.ui.client.util.PersistentContinousRpcCommand;
import com.dumontierlab.ontocreator.ui.client.util.RpcCommandPool;
import com.dumontierlab.ontocreator.ui.client.view.OntologiesListView;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DataProxy;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;

public class OntologiesList extends Composite {

	public static final String ONTOLOGY_NAME_DATA_INDEX = "ontologyName";

	private static final RecordDef recordDef = new RecordDef(new FieldDef[] { new StringFieldDef(
			ONTOLOGY_NAME_DATA_INDEX) });

	private final OntologiesListView view;
	private final Store store;
	private final RpcCommandPool rpcPool;

	public OntologiesList() {
		rpcPool = new RpcCommandPool();
		store = new Store(new ArrayReader(recordDef));
		view = new OntologiesListView(this);
		initWidget(view);
		initRpc();
	}

	private void initRpc() {
		rpcPool.addRpcCommand(new PersistentContinousRpcCommand<Set<String>>() {

			private final OntologyServiceAsync service = OntologyService.Util.getInstace();

			@Override
			protected void rpcCall(AsyncCallback<Set<String>> callback) {
				service.getLoadedOntologies(callback);
			}

			@Override
			protected void rpcFail(Throwable caught) {
				UserMessage.serverError("Unable to obtain list of loaded ontologies from the server", caught);
			}

			@Override
			protected void rpcReturn(Set<String> result) {
				DataProxy proxy = new MemoryProxy(new Object[][] { result.toArray() });
				store.setDataProxy(proxy);
				store.reload();
				UiEventBroker.getInstance().publish(
						new InputOntologiesChangedEvent(result.toArray(new String[result.size()])));
			}
		});
	}

	public Store getStore() {
		return store;
	}

	@Override
	protected void onLoad() {
		rpcPool.suspendAllNonBackgroundRpc();
	}
}

package com.dumontierlab.ontocreator.ui.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {

	void getColumns(AsyncCallback<List<String>> callback);

}

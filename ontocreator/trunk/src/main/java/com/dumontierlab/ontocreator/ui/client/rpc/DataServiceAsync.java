package com.dumontierlab.ontocreator.ui.client.rpc;

import java.util.Collection;
import java.util.List;

import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingBean;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {

	void getColumns(AsyncCallback<List<String>> callback);

	void setColumnMappings(Collection<ColumnMappingBean> mappings, AsyncCallback<Void> callback);
}

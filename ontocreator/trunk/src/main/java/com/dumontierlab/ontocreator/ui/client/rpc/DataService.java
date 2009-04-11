package com.dumontierlab.ontocreator.ui.client.rpc;

import java.util.Collection;
import java.util.List;

import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingBean;
import com.dumontierlab.ontocreator.ui.client.rpc.exception.ServiceException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("DataService")
public interface DataService extends RemoteService {

	List<String> getColumns() throws ServiceException;

	void setColumnMappings(Collection<ColumnMappingBean> mappings) throws ServiceException;

	class Util {
		private static DataServiceAsync instance;

		public static DataServiceAsync getInstance() {
			if (instance == null) {
				instance = (DataServiceAsync) GWT.create(DataService.class);
			}
			return instance;
		}
	}

}

package com.dumontierlab.ontocreator.ui.server.rpc;

import java.util.List;

import com.dumontierlab.ontocreator.model.TabFile;
import com.dumontierlab.ontocreator.ui.client.rpc.DataService;
import com.dumontierlab.ontocreator.ui.client.rpc.exception.ServiceException;
import com.dumontierlab.ontocreator.ui.server.rpc.util.SerializationHelper;
import com.dumontierlab.ontocreator.ui.server.session.ClientSession;
import com.dumontierlab.ontocreator.ui.server.session.SessionHelper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DataServiceImpl extends RemoteServiceServlet implements DataService {

	public List<String> getColumns() throws ServiceException {
		ClientSession session = getClientSession();
		TabFile tabFile = session.getTabFile();
		if (tabFile == null) {
			throw new ServiceException("No tab files have been uploaded.");
		}
		return SerializationHelper.asList(tabFile.getColumnNames());
	}

	private ClientSession getClientSession() {
		return SessionHelper.getClientSession(getThreadLocalRequest());
	}

}

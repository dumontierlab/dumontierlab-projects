package com.dumontierlab.ontocreator.ui.server.rpc;

import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;

import org.semanticweb.owl.model.OWLOntology;

import com.dumontierlab.ontocreator.engine.OntoCreatorEngine;
import com.dumontierlab.ontocreator.inject.InjectorHelper;
import com.dumontierlab.ontocreator.model.TabFile;
import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingBean;
import com.dumontierlab.ontocreator.ui.client.rpc.DataService;
import com.dumontierlab.ontocreator.ui.client.rpc.exception.ServiceException;
import com.dumontierlab.ontocreator.ui.server.rpc.util.SerializationHelper;
import com.dumontierlab.ontocreator.ui.server.session.ClientSession;
import com.dumontierlab.ontocreator.ui.server.session.SessionHelper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;

public class DataServiceImpl extends RemoteServiceServlet implements DataService {

	private OntoCreatorEngine engine;

	@Inject
	public void setEngine(OntoCreatorEngine engine) {
		this.engine = engine;
	}

	@Override
	public void init() throws ServletException {
		super.init();
		InjectorHelper.inject(this);
	}

	public List<String> getColumns() throws ServiceException {
		ClientSession session = getClientSession();
		TabFile tabFile = session.getTabFile();
		if (tabFile == null) {
			throw new ServiceException("No tab files have been uploaded.");
		}
		return SerializationHelper.asList(tabFile.getColumnNames());
	}

	public void setColumnMappings(Collection<ColumnMappingBean> mappings) throws ServiceException {
		ClientSession clientSession = getClientSession();
		OWLOntology outputOntology = clientSession.getOuputOntology();
		if (outputOntology == null) {
			throw new ServiceException("The output ontology has not been created");
		}
		try {
			engine.generateOntology(outputOntology, clientSession.getImports(), clientSession.getTabFile(), mappings,
					clientSession.getInputOntologyManager());
		} catch (Exception e) {
			throw new ServiceException("A error occurred while generating the output ontology", e);
		}
	}

	private ClientSession getClientSession() {
		return SessionHelper.getClientSession(getThreadLocalRequest());
	}

}

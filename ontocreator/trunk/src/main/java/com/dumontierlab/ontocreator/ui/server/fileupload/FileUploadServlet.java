package com.dumontierlab.ontocreator.ui.server.fileupload;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.semanticweb.owl.model.OWLOntology;

import com.dumontierlab.ontocreator.engine.OntoCreatorEngine;
import com.dumontierlab.ontocreator.inject.InjectorHelper;
import com.dumontierlab.ontocreator.io.TabFileInputReaderImpl;
import com.dumontierlab.ontocreator.model.RecordSet;
import com.dumontierlab.ontocreator.ui.client.util.Constants;
import com.dumontierlab.ontocreator.ui.server.session.SessionHelper;
import com.google.inject.Inject;

public class FileUploadServlet extends HttpServlet {

	private OntoCreatorEngine engine;

	@Override
	public void init() throws ServletException {
		super.init();
		InjectorHelper.inject(this);
	}

	@Inject
	public void setEngine(OntoCreatorEngine _engine) {
		engine = _engine;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String type = req.getParameter(Constants.FILE_TYPE_PARAMETER);
		InputStream in = getUploadFile(req);
		TabFileInputReaderImpl reader;

		if (in != null) {
			if (Constants.TAB_FILE_TYPE.equals(type)) {
				reader = new TabFileInputReaderImpl("\t");
				RecordSet rset = reader.read(in, true);
				try {
					OWLOntology ontology = engine.buildInitialOnthology(rset);
					SessionHelper.getClientSession(req).addOntology(ontology);
				} catch (Exception e) {
					resp.sendError(
							HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Error while creating ontology: " + e.getMessage());
				}

			} else if (Constants.ONTOLOGY_FILE_TYPE.equals(type)) {

			}
		}
	}

	private InputStream getUploadFile(HttpServletRequest request)
			throws IOException {

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			List<FileItem> items = upload.parseRequest(request);
			if (items.isEmpty()) {
				return null;
			}

			return items.get(0).getInputStream();

		} catch (FileUploadException e) {
			// TODO
			return null;
		}

	}
}

package com.dumontierlab.ontocreator.ui.server.servlet;

import java.io.File;
import java.io.IOException;
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

import com.dumontierlab.ontocreator.model.TabFile;
import com.dumontierlab.ontocreator.ui.client.util.Constants;
import com.dumontierlab.ontocreator.ui.server.session.ClientSession;
import com.dumontierlab.ontocreator.ui.server.session.SessionHelper;

public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter(Constants.FILE_TYPE_PARAMETER);
		File uploadedFile = getUploadFile(request);

		if (uploadedFile != null) {
			if (Constants.TAB_FILE_TYPE.equals(type)) {
				try {
					ClientSession session = SessionHelper.getClientSession(request);
					session.setTabFile(new TabFile(uploadedFile.toURL(), "\t"));
				} catch (Exception e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while reading tabFile: "
							+ e.getMessage());
				}

			} else if (Constants.ONTOLOGY_FILE_TYPE.equals(type)) {
				try {
					ClientSession session = SessionHelper.getClientSession(request);
					OWLOntology ontology = session.getInputOntologyManager().loadOntology(uploadedFile.toURI());
					session.addInputOntology(ontology);
				} catch (Exception e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while creating ontology: "
							+ e.getMessage());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private File getUploadFile(HttpServletRequest request) throws IOException {

		File tmpFile = File.createTempFile("ontolocreator-tabFile", ".tab");
		tmpFile.deleteOnExit();

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			List<FileItem> items = upload.parseRequest(request);
			if (items.isEmpty()) {
				return null;
			}
			try {
				items.get(0).write(tmpFile);
			} catch (Exception e) {
				throw new IOException("Error while writing uploaded file to tempporary file on disk.");
			}
			return tmpFile;

		} catch (FileUploadException e) {
			// TODO
			return null;
		}

	}
}

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

import com.dumontierlab.ontocreator.ui.client.util.Constants;

public class FileUploadServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String type = req.getParameter(Constants.FILE_TYPE_PARAMETER);
		InputStream in = getUploadFile(req);
		if (in != null) {
			if (Constants.TAB_FILE_TYPE.equals(type)) {
				System.out.println("is a tab file");
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

			System.out.println(items.get(0).getContentType());
			return items.get(0).getInputStream();

		} catch (FileUploadException e) {
			// TODO
			return null;
		}

	}
}

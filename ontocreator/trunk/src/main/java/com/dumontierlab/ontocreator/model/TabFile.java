package com.dumontierlab.ontocreator.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TabFile {

	private final URL fileUrl;
	private final String[] columnNames;
	private final String delimeter;
	private final boolean skipFirstLine;

	public TabFile(URL fileUrl, String delimeter) throws IOException {
		this(fileUrl, readColumnNames(fileUrl, delimeter), delimeter, true);
	}

	public TabFile(URL fileUrl, String[] columnNames, String delimeter) {
		this(fileUrl, columnNames, delimeter, false);
	}

	public TabFile(URL fileUrl, String[] columnNames, String delimeter, boolean skipFirstLine) {
		this.fileUrl = fileUrl;
		this.columnNames = columnNames;
		this.delimeter = delimeter;
		this.skipFirstLine = skipFirstLine;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public String getColumnName(int index) {
		return columnNames[index];
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public BufferedReader getReader() throws IOException {
		BufferedReader buffer = createBufferedReader(fileUrl);
		if (skipFirstLine) {
			buffer.readLine();
		}
		return buffer;
	}

	public String[] readRow(BufferedReader buffer) throws IOException {
		String line = buffer.readLine();
		if (line == null) {
			return null;
		}
		return line.split(delimeter);
	}

	private static BufferedReader createBufferedReader(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		connection.connect();
		InputStreamReader reader = new InputStreamReader(connection.getInputStream());
		return new BufferedReader(reader);
	}

	private static String[] readColumnNames(URL url, String delimeter) throws IOException {
		BufferedReader buffer = createBufferedReader(url);
		String firstLine = buffer.readLine();
		if (firstLine == null) {
			throw new IOException("The url: " + url + " does not contain any data.");
		}
		buffer.close();
		String[] columnNames = firstLine.split(delimeter);
		return columnNames;
	}
}

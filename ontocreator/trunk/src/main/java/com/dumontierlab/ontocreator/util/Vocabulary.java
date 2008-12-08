package com.dumontierlab.ontocreator.util;

import java.net.URI;

public enum Vocabulary {

	ONTOCREATOR_NAMESPACE("http://dumontierlab.com/ontocreator/"),
	ROW(ONTOCREATOR_NAMESPACE + "Row"),
	COLUMN(ONTOCREATOR_NAMESPACE+"Column"),
	CELL(ONTOCREATOR_NAMESPACE+"Cell"),
	HAS_PART("http://purl.org/dc/terms/hasPart"),
	IS_PART_OF("http://purl.org/dc/terms/isPartOf"),
	VALUE("http://www.w3.org/1999/02/22-rdf-syntax-ns#value");

	private final URI uri;

	private Vocabulary(String uri) {
		this.uri = URI.create(uri);
	}

	public URI uri() {
		return uri;
	}

	@Override
	public String toString() {
		return uri.toString();
	}
}

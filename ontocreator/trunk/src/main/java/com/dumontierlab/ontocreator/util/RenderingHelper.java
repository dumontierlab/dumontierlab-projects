package com.dumontierlab.ontocreator.util;

import java.net.URI;

public class RenderingHelper {

	public static String getLabelFromUri(URI uri) {
		String label = uri.getFragment();
		if (label != null) {
			return label;
		}
		String uriString = uri.toString();
		if (uriString.endsWith("/")) {
			uriString = uriString.substring(0, uriString.length() - 1);
		}
		int i = uriString.lastIndexOf("/");
		if (i != -1) {
			return uriString.substring(i + 1);
		}
		return uri.toString();

	}

}

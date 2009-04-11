package com.dumontierlab.ontocreator.ui.client.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.dumontierlab.ontocreator.ui.client.model.OWLClassBean;

public class CollectionsUtil {

	public static void sortAlphabetical(List<OWLClassBean> classes) {
		Collections.sort(classes, new Comparator<OWLClassBean>() {
			public int compare(OWLClassBean o1, OWLClassBean o2) {
				return o1.getLabel().compareTo(o2.getLabel());
			}
		});
	}

}

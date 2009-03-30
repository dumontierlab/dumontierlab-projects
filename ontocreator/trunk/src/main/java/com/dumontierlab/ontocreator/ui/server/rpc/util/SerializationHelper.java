package com.dumontierlab.ontocreator.ui.server.rpc.util;

import java.util.ArrayList;
import java.util.List;

public class SerializationHelper {

	public static <E> List<E> asList(E[] array) {
		ArrayList<E> list = new ArrayList<E>(array.length);
		for (E element : array) {
			list.add(element);
		}
		return list;
	}

}

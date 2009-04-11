package com.dumontierlab.ontocreator.ui.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.ListBox;

public class TypedListBox<E> extends ListBox {

	private final List<E> values = new ArrayList<E>();

	@Override
	public void addItem(String item) {
		assert false : "You can only add items using addItem(Stirng, E)";
	}

	@Override
	public void addItem(String item, String value) {
		assert false : "You can only add items using addItem(Stirng, E)";
	}

	public void addItem(String item, E value) {
		values.add(value);
		super.addItem(item);
	}

	public E getSelectedValue() {
		return values.get(getSelectedIndex());
	}
}

package com.dumontierlab.ontocreator.model;

public class FieldImpl<E> implements Field<E> {

	private String name;
	private E value;

	public FieldImpl() {
		name = null;
		value = null;
	}

	public FieldImpl(String _name, E _value) {
		name = _name;
		value = _value;
	}

	public String getName() {
		return name;
	}

	public E getValue() {
		return value;
	}

	public void setName(String _name) {
		this.name = _name;
	}

	public void setValue(E _value) {
		this.value = _value;
	}

}

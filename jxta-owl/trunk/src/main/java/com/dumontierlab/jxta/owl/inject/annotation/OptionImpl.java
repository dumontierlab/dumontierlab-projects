package com.dumontierlab.jxta.owl.inject.annotation;

import java.lang.annotation.Annotation;

public class OptionImpl implements Option {

	private final String value;

	public OptionImpl(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Option.class;
	}

	@Override
	public int hashCode() {
		// This is specified in java.lang.Annotation.
		return 127 * "value".hashCode() ^ value().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Option)) {
			return false;
		}
		Option other = (Option) o;
		return value().equals(other.value());
	}

	@Override
	public String toString() {
		return "@" + Option.class.getName() + "(value=" + value() + ")";
	}
}

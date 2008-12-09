package com.dumontierlab.ontocreator.mapping;

import java.util.Collections;

import com.dumontierlab.ontocreator.mapping.function.RuntimeFunctionException;

public class BoundMapping extends AbstractMapping {

	private final String uri;

	public BoundMapping(String mappingName, String uri) {
		super(mappingName);
		this.uri = uri;
	}

	public void apply() throws RuntimeFunctionException {
		super.apply(Collections.singletonList(uri));
	}

	@Override
	public String toString() {
		return "X = <"+uri+"> \n"+super.toString();
	}

}

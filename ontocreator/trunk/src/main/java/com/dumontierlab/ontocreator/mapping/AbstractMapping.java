package com.dumontierlab.ontocreator.mapping;

import java.util.ArrayList;
import java.util.List;

import com.dumontierlab.ontocreator.mapping.function.Function;
import com.dumontierlab.ontocreator.mapping.function.RuntimeFunctionException;

public abstract class AbstractMapping implements Mapping {

	private final String name;
	private final List<Function> functions;

	public AbstractMapping(String name) {
		this.name = name;
		functions = new ArrayList<Function>();
	}

	public String getName() {
		return name;
	}

	public void add(Function function) {
		functions.add(function);
	}

	public void remove(Function function) {
		functions.remove(function);
	}

	protected void apply(List<String> input) throws RuntimeFunctionException {
		for (Function f : functions) {
			input = f.apply(input);
		}
	}

	@Override
	public String toString() {
		String arg = "X";
		for (Function f : functions) {
			arg = f.toString(arg);
		}
		return arg;
	}
}

package com.dumontierlab.ontocreator.rule;

import java.util.ArrayList;
import java.util.List;

import com.dumontierlab.ontocreator.rule.function.Function;

public class Rule {

	private final String name;
	private final List<Function> functions;

	public Rule(String name) {
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

	@Override
	public String toString() {
		String arg = "X";
		for (Function f : functions) {
			arg = f.toString(arg);
		}
		return arg;
	}
}

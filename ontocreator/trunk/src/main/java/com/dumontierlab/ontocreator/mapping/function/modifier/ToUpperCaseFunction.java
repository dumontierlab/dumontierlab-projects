package com.dumontierlab.ontocreator.mapping.function.modifier;

import java.util.ArrayList;
import java.util.List;

import com.dumontierlab.ontocreator.mapping.function.Function;
import com.dumontierlab.ontocreator.mapping.function.RuntimeFunctionException;

public class ToUpperCaseFunction implements Function {

	public List<String> apply(List<String> input) throws RuntimeFunctionException {
		List<String> results = new ArrayList<String>();
		for (String inputString : input) {
			results.add(inputString.toUpperCase());
		}
		return results;
	}

	public String toString(String arg) {
		return "toUpperCase(" + arg + ")";
	}
}

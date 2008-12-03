package com.dumontierlab.ontocreator.rule.function.modifier;

import java.util.ArrayList;
import java.util.List;

import com.dumontierlab.ontocreator.rule.function.Function;
import com.dumontierlab.ontocreator.rule.function.RuntimeFunctionException;

public class ToCamelCaseFunction implements Function {

	public List<String> apply(List<String> input) throws RuntimeFunctionException {
		List<String> result = new ArrayList<String>();
		for (String inputString : input) {
			String[] parts = inputString.split("\\s");
			StringBuilder buffer = new StringBuilder();
			for (String part : parts) {
				if (parts.length > 0) {
					buffer.append(Character.toUpperCase(part.charAt(0)));
					if (part.length() > 1) {
						buffer.append(part.substring(1).toLowerCase());
					}
				}
			}
			result.add(buffer.toString());
		}
		return result;
	}

	public String toString(String arg) {
		return "toCamelCase(" + arg + ")";
	}
}

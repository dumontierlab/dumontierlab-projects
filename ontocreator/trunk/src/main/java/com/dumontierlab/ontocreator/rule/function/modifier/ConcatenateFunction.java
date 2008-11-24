package com.dumontierlab.ontocreator.rule.function.modifier;

import java.util.ArrayList;
import java.util.List;

import com.dumontierlab.ontocreator.rule.function.Function;
import com.dumontierlab.ontocreator.rule.function.RuntimeFunctionException;

public class ConcatenateFunction implements Function {

	public enum ConcatenatePosition {
		BEGINING, END
	};

	private final String string;
	private final ConcatenatePosition position;

	public ConcatenateFunction(String string, ConcatenatePosition position) {
		this.string = string;
		this.position = position;
	}

	public List<String> apply(List<String> input) throws RuntimeFunctionException {
		List<String> resutls = new ArrayList<String>();
		for (String inputString : input) {
			if (position.equals(ConcatenatePosition.BEGINING)) {
				resutls.add(inputString.concat(string));
			} else {
				resutls.add(string.concat(inputString));
			}
		}
		return resutls;
	}

}

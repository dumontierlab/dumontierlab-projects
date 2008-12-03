package com.dumontierlab.ontocreator.rule.function;

import java.util.List;

public interface Function {

	List<String> apply(List<String> input) throws RuntimeFunctionException;

	String toString(String arg);
}

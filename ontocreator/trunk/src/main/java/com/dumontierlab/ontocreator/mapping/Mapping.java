package com.dumontierlab.ontocreator.mapping;

import com.dumontierlab.ontocreator.mapping.function.Function;
import com.dumontierlab.ontocreator.mapping.function.RuntimeFunctionException;

public interface Mapping {

	enum MappingName { INSTANCE_MAPPING, CLASS_MAPPING, BOUND_MAPPING };

	public abstract String getName();

	public abstract void add(Function function);

	public abstract void remove(Function function);

	public abstract void apply() throws RuntimeFunctionException;

}
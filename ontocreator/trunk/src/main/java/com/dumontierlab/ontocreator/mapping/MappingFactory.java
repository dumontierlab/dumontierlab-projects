package com.dumontierlab.ontocreator.mapping;

public interface MappingFactory {

	InstanceMapping createInstanceMapping();

	ClassMapping createClassMapping();

	BoundMapping createBoundMapping(String uri);

}

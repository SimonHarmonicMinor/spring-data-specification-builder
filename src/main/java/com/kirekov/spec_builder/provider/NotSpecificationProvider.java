package com.kirekov.spec_builder.provider;

/**
 * Provides fluent API to inverse defined query operations
 *
 * @param <Entity>   the type of the entity
 * @param <Provider> the type of the specification provider
 */
public interface NotSpecificationProvider<Entity, Provider extends NotSpecificationProvider<Entity, Provider>> extends CombinedSpecificationProvider<Entity, Provider> {
    CombinedSpecificationProvider<Entity, Provider> not();
}

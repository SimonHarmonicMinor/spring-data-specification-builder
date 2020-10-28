package com.kirekov.spec_builder.provider;

/**
 * Provides fluent API to build queries. Combines all specification providers
 *
 * @param <Entity>   the type of the entity
 * @param <Provider> the type of the specification provider
 */
public interface CombinedSpecificationProvider<Entity, Provider extends CombinedSpecificationProvider<Entity, Provider>> extends
        CustomSpecificationProvider<Entity, Provider>,
        EqSpecificationProvider<Entity, Provider>,
        LikeSpecificationProvider<Entity, Provider> {
}

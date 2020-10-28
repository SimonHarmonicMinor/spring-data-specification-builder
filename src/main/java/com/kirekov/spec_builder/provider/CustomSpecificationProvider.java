package com.kirekov.spec_builder.provider;

import org.springframework.data.jpa.domain.Specification;

/**
 * Provides fluent API to build queries with custom {@link Specification}
 *
 * @param <Entity>   the type of the entity
 * @param <Provider> the type of the specification provider
 */
public interface CustomSpecificationProvider<Entity, Provider extends CustomSpecificationProvider<Entity, Provider>> {
    Provider specification(Specification<Entity> specification);
}

package com.kirekov.spec_builder.builder;

import com.kirekov.spec_builder.provider.NotSpecificationProvider;
import org.springframework.data.jpa.domain.Specification;

/**
 * Allows to build defined specification
 *
 * @param <Entity>   the type of the entity
 * @param <Provider> the type of the specification provider
 */
public interface SpecificationBuilder<Entity, Provider extends NotSpecificationProvider<Entity, Provider>> extends NotSpecificationProvider<Entity, Provider> {
    Specification<Entity> build();
}

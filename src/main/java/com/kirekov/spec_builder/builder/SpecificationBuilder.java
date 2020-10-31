package com.kirekov.spec_builder.builder;

import com.kirekov.spec_builder.provider.NotSpecificationProvider;
import org.springframework.data.jpa.domain.Specification;

/**
 * Allows to build defined specification
 *
 * @param <Entity>   the type of the entity
 */
public interface SpecificationBuilder<Entity> {
    Specification<Entity> build();
}

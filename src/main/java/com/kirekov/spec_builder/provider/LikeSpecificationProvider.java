package com.kirekov.spec_builder.provider;

import com.kirekov.spec_builder.builder.SpecificationBuilder;

import javax.persistence.criteria.Expression;
import javax.persistence.metamodel.Attribute;

/**
 * Provides fluent API to build like queries
 *
 * @param <Entity>   the type of the entity
 * @param <Provider> the type of the specification provider
 * @see javax.persistence.criteria.CriteriaBuilder#like(Expression, String)
 */
public interface LikeSpecificationProvider<Entity, Provider extends LikeSpecificationProvider<Entity, Provider>> {
    Provider like(String field, String pattern);

    Provider like(Attribute<Entity, ?> field, String pattern);

    Provider likeIgnoreCase(String field, String pattern);

    Provider likeIgnoreCase(Attribute<Entity, ?> field, String pattern);
}

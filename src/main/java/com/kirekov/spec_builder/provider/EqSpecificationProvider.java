package com.kirekov.spec_builder.provider;

import com.kirekov.spec_builder.builder.SpecificationBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.metamodel.Attribute;

/**
 * Provides fluent API to build queries with <code>eq</code> expressions
 *
 * @param <Entity>   the type of the entity
 * @param <Provider> the type of the specification provider
 * @see CriteriaBuilder#equal(Expression, Object)
 */
public interface EqSpecificationProvider<Entity, Provider extends EqSpecificationProvider<Entity, Provider>> extends SpecificationBuilder<Entity> {
    Provider eq(String field, Object value);

    Provider eq(Attribute<Entity, ?> field, Object value);
}
